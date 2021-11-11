#See https://aka.ms/containerfastmode to understand how Visual Studio uses this Dockerfile to build your images for faster debugging.

FROM mcr.microsoft.com/dotnet/aspnet:3.1 AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:3.1 AS build
WORKDIR /src
COPY ["CatenaX.NetworkServices.MembershipCompany.Service/CatenaX.NetworkServices.MembershipCompany.Service.csproj", "CatenaX.NetworkServices.MembershipCompany.Service/"]
RUN dotnet restore "CatenaX.NetworkServices.MembershipCompany.Service/CatenaX.NetworkServices.MembershipCompany.Service.csproj"
COPY . .
WORKDIR "/src/CatenaX.NetworkServices.MembershipCompany.Service"
RUN dotnet build "CatenaX.NetworkServices.MembershipCompany.Service.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "CatenaX.NetworkServices.MembershipCompany.Service.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "CatenaX.NetworkServices.MembershipCompany.Service.dll"]