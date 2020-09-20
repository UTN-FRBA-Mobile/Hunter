FROM mcr.microsoft.com/dotnet/core/sdk:3.1-buster AS server

ENV ASPNETCORE_Environment=Production

WORKDIR /server

COPY . ./

RUN dotnet publish -c Release -o publish

CMD ["dotnet","ef migrations add initial --startup-project publish/Hunter.dll --context HunterContext"]