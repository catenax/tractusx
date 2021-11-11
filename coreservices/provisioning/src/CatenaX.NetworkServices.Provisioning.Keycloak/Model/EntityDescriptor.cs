using System.Collections.Generic;
using System.Xml.Serialization;

namespace CatenaX.NetworkServices.Provisioning.Keycloak.Models
{
    [XmlRoot(Namespace = "urn:oasis:names:tc:SAML:2.0:metadata")]
    public class EntityDescriptor
    {
        [XmlAttribute]
        public string entityID;
        [XmlElement(Namespace = "urn:oasis:names:tc:SAML:2.0:metadata")]
        public IDPSSODescriptor IDPSSODescriptor;
    }
    public class IDPSSODescriptor
    {
        public string WantAuthnRequestsSigned;
        public string protocolSupportEnumeration;

        [XmlElement(Namespace = "urn:oasis:names:tc:SAML:2.0:metadata")]
        public KeyDescriptor KeyDescriptor;

        [XmlElement(Namespace = "urn:oasis:names:tc:SAML:2.0:metadata")]
        public ArtifactResolutionService ArtifactResolutionService;

        [XmlElement(Namespace = "urn:oasis:names:tc:SAML:2.0:metadata")]
        public List<BindingLocation> SingleLogoutService;

        [XmlElement(Namespace = "urn:oasis:names:tc:SAML:2.0:metadata")]
        public List<BindingLocation> NameIDFormat;

        [XmlElement(Namespace = "urn:oasis:names:tc:SAML:2.0:metadata")]
        public List<BindingLocation> SingleSignOnService;
    }
    public class KeyDescriptor
    {
        [XmlAttribute]
        public string use;

        [XmlElement(Namespace = "http://www.w3.org/2000/09/xmldsig#")]
        public KeyInfo KeyInfo;
    }
    public class KeyInfo
    {
        [XmlElement(Namespace = "http://www.w3.org/2000/09/xmldsig#")]
        public string KeyName;

        [XmlElement(Namespace = "http://www.w3.org/2000/09/xmldsig#")]
        public X509Data X509Data;
    }
    public class X509Data
    {
        [XmlElement(Namespace = "http://www.w3.org/2000/09/xmldsig#")]
        public string X509Certificate;
    }
    public class BindingLocation
    {
        [XmlAttribute]
        public string Binding;

        [XmlAttribute]
        public string Location;
    }
    public class ArtifactResolutionService:BindingLocation
    {
        [XmlAttribute]
        public int index;
    }
}
