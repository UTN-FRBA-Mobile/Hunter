package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.setToolbarTitle
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.service.APIAdapter
import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.service.models.User
import com.utn.frba.desarrollomobile.hunter.ui.activity.MainActivity
import com.utn.frba.desarrollomobile.hunter.utils.LoginHandler
import com.utn.frba.desarrollomobile.hunter.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.view_listitem_win_game.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var gameViewModel: GameViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()

        if (LoginHandler.USERID == 0) {
            val callGetUserResponse =
                APIAdapter.getAPI().getUser()

            callGetUserResponse.enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    print("throw Message" + t.message)
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

        gameViewModel = ViewModelProvider(requireActivity()).get(GameViewModel::class.java)

        var viewAdapter = GamesAdapter(myDataset) { game ->
            gameViewModel.setGame(game)
            showFragment(HistoryDetailFragment(), true)
        }

        val callGameHistoryResponse =
            APIAdapter.getAPI().getMyHistoryGames()

        callGameHistoryResponse.enqueue(object : Callback<ArrayList<Game>> {
            override fun onFailure(call: Call<ArrayList<Game>>, t: Throwable) {
                print("throw Message" + t.message)
                hideLoading()
            }

            override fun onResponse(
                call: Call<ArrayList<Game>>,
                response: Response<ArrayList<Game>>
            ) {
                val body = response.body()
                body?.let {
                    myDataset = it.toMutableList()

                    viewAdapter = GamesAdapter(myDataset) { game ->
                        gameViewModel.setGame(game)
                        showFragment(HistoryDetailFragment(), true)
                    }

                    recyclerView = recycler_history_games.apply {
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }
                    hideLoading()
                }
            }
        })

        recyclerView = recycler_history_games.apply {
            layoutManager = viewManager
            adapter = viewAdapter
            val dividerItemDecoration = DividerItemDecoration(
                this.context,
                LinearLayoutManager.VERTICAL
            )
            addItemDecoration(dividerItemDecoration)
        }
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle(getString(R.string.historyGame))
    }

    private fun showLoading() {
        (activity as MainActivity).showLoading("Cargando...")
    }

    private fun hideLoading() {
        (activity as MainActivity).hideLoading()
    }
}

class GamesAdapter(
    private val myDataset: MutableList<Game>,
    private val onGameClickedListener: (Game) -> Unit
) :
    RecyclerView.Adapter<GamesAdapter.MyViewHolder>() {

    override fun getItemCount() = myDataset.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_listitem_win_game, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val game = myDataset[position]

        with(holder) {

            gameParent.setOnClickListener { onGameClickedListener.invoke(game) }

            gameDate.text = game.endDatetime?.substring(0, 10)
            if (game.winId == LoginHandler.USERID) {
                gameWinner.text = "GANÉ!!!"
            } else {
                gameWinner.text = "Perdí..."
            }

            if (!game.photo.isNullOrEmpty()) {
                try {
                    Picasso.get().load(game.photo).into(gamePhoto)
                } catch (e: Exception) {
                }
            }
        }

    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gameParent: ViewGroup = view.game_item_parent
        val gameDate: TextView = view.game_date
        val gameWinner: TextView = view.game_winner
        val gamePhoto: ImageView = view.game_photo
    }
}