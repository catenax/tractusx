using System.Threading.Tasks;
using Microsoft.Extensions.Options;
using MimeKit;
using MailKit.Net.Smtp;

namespace CatenaX.NetworkServices.Mailing.SendMail
{
    public class SendMail : ISendMail
    {
        private MailSettings _MailSettings;

        public SendMail(IOptions<MailSettings> mailSettings)
        {
            _MailSettings = mailSettings.Value;
        }

        Task ISendMail.Send(string sender, string recipient, string subject, string body, bool useHtml = false)
        {
            var message = new MimeMessage();
            message.From.Add(MailboxAddress.Parse(sender));
            message.To.Add(MailboxAddress.Parse(recipient));
            message.Subject = subject;
            if(useHtml)
            {
                message.Body = new TextPart("html") { Text = body };
            }
            else
            {
                message.Body = new TextPart("plain") { Text = body };
            }
            return _send(message);
        }

        private async Task _send(MimeMessage message)
        {
            var client = new SmtpClient();
            await client.ConnectAsync(_MailSettings.SmtpHost, 587);
            await client.AuthenticateAsync(_MailSettings.SmtpUser, _MailSettings.SmtpPassword);
            await client.SendAsync(message);
            await client.DisconnectAsync(true);
        }
    }
}
