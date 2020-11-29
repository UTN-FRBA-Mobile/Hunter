package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.setToolbarTitle
import com.utn.frba.desarrollomobile.hunter.service.APIAdapter
import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.service.models.User
import com.utn.frba.desarrollomobile.hunter.utils.LoginHandler
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.view_listitem_lost_game.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (LoginHandler.USERID == 0) {
            var callGetUserResponse =
                APIAdapter.getAPI().getUser()

            callGetUserResponse.enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    print("throw Message" + t.message)
                    register_password_confirmation.error = "Error reading JSON"
                }

                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    val body = response.body()
                    body?.let {
                        LoginHandler.USERID = it.id
                    }
                }
            })
        }

        var myDataset = ArrayList<Game>().toMutableList()

        val viewManager = LinearLayoutManager(this.context)
        var viewAdapter = GamesAdapter(myDataset)

        var callGameHistoryResponse =
            APIAdapter.getAPI().getMyHistoryGames()

        callGameHistoryResponse.enqueue(object : Callback<ArrayList<Game>> {
            override fun onFailure(call: Call<ArrayList<Game>>, t: Throwable) {
                print("throw Message" + t.message)
                register_password_confirmation.error = "Error reading JSON"
            }

            override fun onResponse(
                call: Call<ArrayList<Game>>,
                response: Response<ArrayList<Game>>
            ) {
                val body = response.body()
                body?.let {
                    myDataset = it.toMutableList()

                    viewAdapter = GamesAdapter(myDataset)

                    recyclerView = recycler_history_games.apply {
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }
                }
            }
        })

        recyclerView = recycler_history_games.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle(getString(R.string.historyGame))
    }
}

class GamesAdapter(private val myDataset: MutableList<Game>) :
    RecyclerView.Adapter<GamesAdapter.MyViewHolder>() {

    override fun getItemCount() = myDataset.size

    override fun getItemViewType(position: Int): Int {
        if(myDataset[position].winId != null)
        {
            val winID: Int = myDataset[position].winId ?: 0
            if (winID == LoginHandler.USERID)
                return 1
        }

        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesAdapter.MyViewHolder {
        val view : View = if(viewType == 1)
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_listitem_win_game, parent, false)
        else
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_listitem_lost_game, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindGame(myDataset[position])
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){

        fun bindGame(game: Game) {
            itemView.game_date.text = game.endDatetime?.substring(0, 10)

            if (!game.photo.isNullOrEmpty()) {
                try {
                    Picasso.get().load(game.photo).into(itemView.game_photo)
                } catch (e:Exception) {}
            }
        }
    }
}