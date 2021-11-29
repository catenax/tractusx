#See https://aka.ms/containerfastmode to understand how Visual Studio uses this Dockerfile to build your images for faster debugging.

FROM mcr.microsoft.com/dotnet/aspnet:3.1 AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:3.1 AS build
WORKDIR /src
COPY ["invitation/src/CatenaX.NetworkServices.Invitation.Service/CatenaX.NetworkServices.Invitation.Service.csproj", "invitation/src/CatenaX.NetworkServices.Invitation.Service/"]
COPY ["mailing/src/CatenaX.NetworkServices.Mailing.Template/CatenaX.NetworkServices.Mailing.Template.csproj", "mailing/src/CatenaX.NetworkServices.Mailing.Template/"]
COPY ["mailing/src/CatenaX.NetworkServices.Mailing.SendMail/CatenaX.NetworkServices.Mailing.SendMail.csproj", "mailing/src/CatenaX.NetworkServices.Mailing.SendMail/"]
COPY ["invitation/src/CatenaX.NetworkServices.Invitation.Identity/CatenaX.NetworkServices.Invitation.Identity.csproj", "invitation/src/CatenaX.NetworkServices.Invitation.Identity/"]
COPY ["mockup/src/CatenaX.NetworkServices.Mockups/CatenaX.NetworkServices.Mockups.csproj", "mockup/src/CatenaX.NetworkServices.Mockups/"]
COPY ["invitation/src/CatenaX.NetworkServices.Invitation.Library/CatenaX.NetworkServices.Invitation.Library.csproj", "invitation/src/CatenaX.NetworkServices.Invitation.Library/"]
RUN dotnet restore "invitation/src/CatenaX.NetworkServices.Invitation.Service/CatenaX.NetworkServices.Invitation.Service.csproj"
COPY . .
WORKDIR "/src/invitation/src/CatenaX.NetworkServices.Invitation.Service"
RUN dotnet build "CatenaX.NetworkServices.Invitation.Service.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "CatenaX.NetworkServices.Invitation.Service.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "CatenaX.NetworkServices.Invitation.Service.dll"]