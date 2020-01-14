/*
 */
package EntregaVehi;

import java.awt.List;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author c.malaver
 */
public class Asociancion {

    public String srtUrlFile;
    private final static String USER_AGENT = "Mozilla/5.0";
    private static HttpURLConnection con;
    public static final String SEPARATOR = ";";
    public static final String QUOTE = "\"";

    public void SendPost(String sParameters) throws Exception {
        //String  fechanueva = fechan();
        String sPassword = "Renting123*";
        String sUser = "maxadmin";
        String url = "https://rentingautomayor-test.maximo.com/maxrest_b1dk/rest/os/RA_ASSET_CONTRACT?_lid=" + sUser + "&_lpwd=" + sPassword;
        String urlParameters = sParameters;
        System.out.println(url + "" + sParameters);
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        try {
            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }
            StringBuilder content;
            int cont = 0;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String line;
                content = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                    cont++;
                }
            } catch (Exception e) {
                System.out.println("Error:" + e.getMessage() + "Line:" + cont);
            }
        } finally {
            con.disconnect();
        }
    }

    public String sParameters(String Placa, String Contrato, String Nit, String Descripcion) {
        String conts = "&RAACTIVO=" + Placa + "&RACONTRATOCLIENTE=" + Contrato + "&RACLIENTE=" + Nit + "&DESCRIPTION=" + Descripcion + "&RAENTREGADO=0";
        return conts;
    }

    public String SendGetContratos(String sParameters) throws Exception {
        //String  fechanueva = fechan();
        String sPassword = "Renting123*";
        String sUser = "maxadmin";
        String Url = "https://rentingautomayor-test.maximo.com/maxrest_b1dk/rest/mbo/RA_CONTRATOCLIENTE?_lid=" + sUser + "&_lpwd=" + sPassword + sParameters;
        String urlParameters = sParameters;
        //System.out.println(Url + "" + sParameters2);

        URL obj = new URL(Url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        //Add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + Url);
        System.out.println("Response Code : " + responseCode);

        StringBuilder response;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                //System.out.println("DATA:"+response.toString());
            }
        }

        //print result
        String Strname1 = "GETcontratos";
        String Candena = response.toString();
        System.out.println(Candena);
        String pathcontrato = CreateXml1(Candena, Strname1);
        return pathcontrato;

    }

    public String sParameters(String Ab) {
        String conts = Ab;
        return conts;
    }

    public String CreateXml1(String sTextXml1, String Strname1) throws IOException {
        File file1 = new File("A:\\netbeans\\" + Strname1 + ".xml");

        if (file1.createNewFile()) {
            System.out.println("File is created!");
        }
        /*else {
            System.out.println("File already exists.");
        }*/

        FileWriter writer = new FileWriter(file1);
        writer.write(sTextXml1);
        writer.close();
        try {
            Leerxmlpapa1(Strname1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file1.getPath();
    }

    public ArrayList<Contrato> Leerxmlpapa1(String Pahtxml1) throws IOException {
        ArrayList<Contrato> Lscontrato = new ArrayList<Contrato>();
        try {
            File file1 = new File(Pahtxml1);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file1);
//String usr = document.getElementsByTagName("DESCRIPTION").item(0).getTextContent();
//String pwd = document.getElementsByTagName("password").item(0).getTextContent(); 
//System.out.println(usr);
            NodeList listaEmpleados = document.getElementsByTagName("RA_CONTRATOCLIENTE");

            for (int temp = 0; temp < (listaEmpleados.getLength()); temp++) {
                Node nodo = listaEmpleados.item(temp);
                // System.out.println("Elemento:" + nodo.getNodeName());
                System.out.println("[NODO NUMERO " + temp + "]:: " + nodo.getNodeType());
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodo;
                    String strdescri = element.getElementsByTagName("DESCRIPTION").item(0).getTextContent();
                    System.out.println(strdescri);
                    String strcod = element.getElementsByTagName("RACODIGOCONTRATO").item(0).getTextContent();
                    System.out.println(strcod);
                    String strcliente = element.getElementsByTagName("RACLIENTE").item(0).getTextContent();
                    System.out.println(strcliente);
                    Contrato objContrato = new Contrato();
                    objContrato.Contrato = strdescri;
                    objContrato.Ncontrato = strcod;
                    objContrato.Nit = strcliente;
                    Lscontrato.add(objContrato);
                }
            }

            System.out.println("Finaliza ejecución de XML");
        } catch (Exception e) {
            System.out.println("[ERROR LEER PAPÁ]: " + e.getMessage());
        }
        return Lscontrato;
    }

    public String SendGetPlacas(String sParameters2) throws Exception {
        //String  fechanueva = fechan();
        String sPassword = "Renting123*";
        String sUser = "maxadmin";
        String Url = "https://rentingautomayor-test.maximo.com/maxrest_b1dk/rest/mbo/ASSET?_lid=" + sUser + "&_lpwd=" + sPassword + sParameters2;
        String urlParameters = sParameters2;
        //System.out.println(Url + "" + sParameters2);

        URL obj = new URL(Url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        //Add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + Url);
        System.out.println("Response Code : " + responseCode);

        StringBuilder response;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                //System.out.println("DATA:"+response.toString());
            }
        }

        //print result
        String Strname = "GETPLACAS";
        String Candena = response.toString();
        System.out.println(Candena);
        String Pathplacas = CreateXml(Candena, Strname);
        return Pathplacas;

    }

    public String sParameters2(String A) {
        String conts = A;
        return conts;
    }

    public static String fechan() {
        Date fecha = new Date();
        String fe = "";
        String hr = "";
        SimpleDateFormat formatoFecha = new SimpleDateFormat("YYYY-MM-dd");
        SimpleDateFormat formatoHora = new SimpleDateFormat("hh-mm-ss");
        fe = formatoFecha.format(fecha.getTime());
        hr = formatoHora.format(fecha.getTime());
        // return formatoFecha.format(fecha);
        //System.out.println(formatoFecha.format(fecha));
        //Leerxmlpapa(formatoFecha);
        String Fch = fe + "HORA" + hr;
        //System.out.println(Fch);

        return Fch;

    }

    public String CreateXml(String sTextXml, String Strname) throws IOException {
        File file = new File("A:\\netbeans\\" + Strname + ".xml");

        if (file.createNewFile()) {
            System.out.println("File is created!");
        }
        /*else {
            System.out.println("File already exists.");
        }*/

        FileWriter writer = new FileWriter(file);
        writer.write(sTextXml);
        writer.close();
        try {
            Leerxmlpapa(Strname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    public ArrayList<Vehiculo> Leerxmlpapa(String Pahtxml) throws IOException {

        ArrayList<Vehiculo> Lsvehiculos = new ArrayList<Vehiculo>();
        try {

            File file = new File(Pahtxml);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
//String usr = document.getElementsByTagName("DESCRIPTION").item(0).getTextContent();
//String pwd = document.getElementsByTagName("password").item(0).getTextContent(); 
//System.out.println(usr);
            NodeList listaEmpleados = document.getElementsByTagName("ASSET");

            for (int temp = 0; temp < (listaEmpleados.getLength()); temp++) {
                Node nodo = listaEmpleados.item(temp);
                // System.out.println("Elemento:" + nodo.getNodeName());
                System.out.println("[NODO NUMERO " + temp + "]:: " + nodo.getNodeType());
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodo;
                    String strPlaca = element.getElementsByTagName("ASSETNUM").item(0).getTextContent();
                    System.out.println(strPlaca);
                    Vehiculo objVehiculo = new Vehiculo();
                    objVehiculo.Placa = strPlaca;
                    Lsvehiculos.add(objVehiculo);
                }
            }
            //Placaslist(strPlaca);
            System.out.println("Finaliza ejecución de XML");

        } catch (Exception e) {
            System.out.println("[ERROR LEER PAPÁ]: " + e.getMessage());
        }
        return Lsvehiculos;
    }

}
