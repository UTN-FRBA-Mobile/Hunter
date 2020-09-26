#See https://aka.ms/containerfastmode to understand how Visual Studio uses this Dockerfile to build your images for faster debugging.

FROM mcr.microsoft.com/dotnet/core/aspnet:3.1-buster-slim AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

FROM mcr.microsoft.com/dotnet/core/sdk:3.1-buster AS build
WORKDIR /src
COPY ["Hunter/Hunter.csproj", "Hunter/"]
RUN dotnet restore "Hunter/Hunter.csproj"
COPY . .
WORKDIR "/src/Hunter"
RUN dotnet build "Hunter.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "Hunter.csproj" -c Release -o /app/publish

ENV DATABASE_URL=postgres://ouaqzwgabbxhxt:0687c2478f0521a595632a35a7638252b71b6db73f35badddf40bcc5fdc2153d@ec2-3-216-92-193.compute-1.amazonaws.com:5432/d5qpniim1huq2a
ENV PORT=80


FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "ef migrations add initial --startup-project publish/Hunter.dll --context HunterContext"]]