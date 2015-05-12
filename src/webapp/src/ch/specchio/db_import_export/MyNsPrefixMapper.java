package ch.specchio.db_import_export;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class MyNsPrefixMapper extends NamespacePrefixMapper
{
  @Override
public String getPreferredPrefix(String uri, String suggest, boolean require)
  {
    if("http://ands.org.au/standards/rif-cs/registryObjects".equals(uri) ){return "";}
    return suggest;
  }

  @Override
public String[] getPreDeclaredNamespaceUris()
  {
    // String[] result = new String[1];
    // result[0] = "http://www.theronyx.com/mdasj/xmldata";
    return new String[0];
  }
}