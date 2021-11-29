using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using Microsoft.Extensions.Options;
using CatenaX.NetworkServices.Mailing.Template.Model;

namespace CatenaX.NetworkServices.Mailing.Template
{
    public class TemplateManager : ITemplateManager
    {
        private readonly TemplateSettings _Templates;

        public TemplateManager(IOptions<TemplateSettings> templateSettings)
        {
            _Templates = templateSettings.Value;
        }

        Mail ITemplateManager.ApplyTemplate(string id, IDictionary<string, string> parameters)
        {
            try
            {
                var Template = _Templates[id];
                return new Mail(
                    replaceValues(Template.Subject,parameters),
                    replaceValues(Template.Body,parameters),
                    Template.html
                );
            }
            catch(ArgumentNullException)
            {
                throw new NoSuchTemplateException(id);
            }
            catch(KeyNotFoundException)
            {
                throw new NoSuchTemplateException(id);
            }
        }

        private string replaceValues(string template, IDictionary<string,string> parameters)
        {
            return Regex.Replace(
                template,
                @"\{(\w+)\}", //replaces any text surrounded by { and }
                m =>
                {
                    string value;
                    return parameters.TryGetValue(m.Groups[1].Value, out value) ? value : "null";
                }
            );
        }
    }
}
