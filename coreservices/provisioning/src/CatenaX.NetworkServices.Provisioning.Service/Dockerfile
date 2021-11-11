#See https://aka.ms/containerfastmode to understand how Visual Studio uses this Dockerfile to build your images for faster debugging.

FROM mcr.microsoft.com/dotnet/aspnet:3.1 AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:3.1 AS build
WORKDIR /src
COPY ["provisioning/src/CatenaX.NetworkServices.Provisioning.Service/CatenaX.NetworkServices.Provisioning.Service.csproj", "provisioning/src/CatenaX.NetworkServices.Provisioning.Service/"]
COPY ["mailing/src/CatenaX.NetworkServices.Mailing.Template/CatenaX.NetworkServices.Mailing.Template.csproj", "mailing/src/CatenaX.NetworkServices.Mailing.Template/"]
COPY ["provisioning/src/CatenaX.NetworkServices.Provisioning.Keycloak/CatenaX.NetworkServices.Provisioning.Keycloak.csproj", "provisioning/src/CatenaX.NetworkServices.Provisioning.Keycloak/"]
COPY ["invitation/src/CatenaX.NetworkServices.Invitation.Identity/CatenaX.NetworkServices.Invitation.Identity.csproj", "invitation/src/CatenaX.NetworkServices.Invitation.Identity/"]
COPY ["provisioning/src/CatenaX.NetworkServices.Provisioning.ActiveDirectory/CatenaX.NetworkServices.Provisioning.ActiveDirectory.csproj", "provisioning/src/CatenaX.NetworkServices.Provisioning.ActiveDirectory/"]
COPY ["mailing/src/CatenaX.NetworkServices.Mailing.SendMail/CatenaX.NetworkServices.Mailing.SendMail.csproj", "mailing/src/CatenaX.NetworkServices.Mailing.SendMail/"]
COPY ["provisioning/src/CatenaX.NetworkServices.Provisioning.Mail/CatenaX.NetworkServices.Provisioning.Mail.csproj", "provisioning/src/CatenaX.NetworkServices.Provisioning.Mail/"]
COPY ["provisioning/src/CatenaX.NetworkServices.Provisioning.Utils/CatenaX.NetworkServices.Provisioning.Utils.csproj", "provisioning/src/CatenaX.NetworkServices.Provisioning.Utils/"]
RUN dotnet restore "provisioning/src/CatenaX.NetworkServices.Provisioning.Service/CatenaX.NetworkServices.Provisioning.Service.csproj"
COPY . .
WORKDIR "/src/provisioning/src/CatenaX.NetworkServices.Provisioning.Service"
RUN dotnet build "CatenaX.NetworkServices.Provisioning.Service.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "CatenaX.NetworkServices.Provisioning.Service.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "CatenaX.NetworkServices.Provisioning.Service.dll"]