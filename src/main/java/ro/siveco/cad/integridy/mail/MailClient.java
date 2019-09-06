package ro.siveco.cad.integridy.mail;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Mailjet client wrapper.
 */
public class MailClient implements MailConstants{

    private static MailClient instance;

    private MailjetClient client;

    private MailClient(){
        client = new MailjetClient(API_KEY, API_SECRET, new ClientOptions("v3.1"));
    }

    public static synchronized MailClient getInstance(){
        if(instance==null){
            instance = new MailClient();
        }
        return instance;
    }

    public MailjetResponse send(Mail email) throws MailjetException, MailjetSocketTimeoutException {
                JSONArray recipientsEmailsArray = new JSONArray();
        for(String recipient:email.getRecipients()){
            recipientsEmailsArray.put(new JSONObject().put("Email", recipient));
        }
        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put (new JSONObject()
                            .put(Emailv31.Message.FROM, new JSONObject()
                                .put("Email", email.getFromAddress())
                                .put("Name", email.getFromName()))
                            .put(Emailv31.Message.TO, recipientsEmailsArray)
                            .put(Emailv31.Message.SUBJECT, email.getSubject())
                            .put(Emailv31.Message.TEXTPART, email.getTextContent())
                            .put(Emailv31.Message.HTMLPART, email.getHtmlContent())));



        return client.post(request);
    }
//        if(email.getTextContent()!=null){
//            request.property(Emailv31.Message.TEXTPART, email.getTextContent());
//        }
//        if(email.getHtmlContent()!=null){
//            request.property(Emailv31.Message.HTMLPART, email.getHtmlContent());
//        }


}
