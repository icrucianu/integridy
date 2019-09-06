package ro.siveco.cad.integridy.mail.messages;

//import ro.siveco.cad.vpc.livinvpc.dao.*;
//import ro.siveco.cad.vpc.livinvpc.jsf.cm.ViewUserApplicationCtrl;
//import ro.siveco.cad.vpc.livinvpc.util.ServerUtils;

import javax.security.auth.Subject;
import ro.siveco.cad.integridy.entities.ResetPassword;
import ro.siveco.cad.integridy.entities.Users;
import ro.siveco.cad.integridy.util.ServerUtils;

public class MailMessages {


    private static final String BASE_URI = ServerUtils.getAppPath();
    private static String avatarSrc = BASE_URI + "/faces/javax.faces.resource/images/mail/avatar.jpg";
    private static String logoSrc = BASE_URI + "/faces/javax.faces.resource/images/mail/logo.png";
    private static String bg = BASE_URI + "/faces/javax.faces.resource/images/mail/bg.jpg";
    private static String header = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "<title>Integridy account information</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<div align=\"center\" style=\"margin:0 auto; max-width:800px;\">\n" +
            "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
            "  <tr>\n" +
            "    <td height=\"22\">&nbsp;</td>\n" +
            "  </tr>\n" +
            "  <tr>\n" +
            "    <td height=\"25\">&nbsp;</td>\n" +
            "  </tr>\n" +
            "  <tr>\n" +
            "    <td height=\"25\" >";
    private static String footerWSignature = "<br/>Heike Vogel-P&ouml;schl<br/>\n" +
            "\n" +
            "\t<div style=\"text-align:left; margin-top:10px;\"><img src=\"" + avatarSrc + "\" border=\"0\" style=\"display:block;\" /></div><br/>\n" +
            "Deputy coordinator of the LIV_IN project<br/>" +
            "Vienna University of Economics and Business" +
            "\t</td>\n" +
            "  </tr>\n" +
            "</div>\n" +
            "</body>\n" +
            "</html>";
    private static String footerNormal = "\t</td>\n" +
            "  </tr></table>\n" +
            "</div>\n" +
            "</body>\n" +
            "</html>";

    
    
        public  static String userChangePassword(ResetPassword resetPassword){
        String confirmLink = BASE_URI + "/faces/accountConfirmation.xhtml?apptoken=" + resetPassword.getToken();
        String confirmText = "Change password for your INTEGRIDY account!.";
//        return confirmLink;
        return        header
                + "<p>Hello " + resetPassword.getUserId().getFirstName() + " " + resetPassword.getUserId().getLastName() + ",</p>"
                + "<br/>"
                + "<p>Welcome to the INTEGRIDY SYSTEM. </p>"
                + "<p>Please click on the following link in order to change passord for your Integridy account:</p>"
                + "<p><a href=\"" +confirmLink +"\" > Change password </a></p>"
                + "<p>Link doesn't work? Copy the following link to your browser bar: </p>"
                + "<p><a href\""+ confirmLink +"\"> "+confirmLink+"</a></p>"
                + "<p>Have a nice day, </p>"
                 + "<p>Integridy Team </p>"
                + footerNormal;
    }
//    //Registration e-mails
    public  static String userConfirmation(Users currentUser){
        String confirmLink = BASE_URI + "/faces/accountConfirmation.xhtml?apptoken=" + currentUser.getToken();
        String confirmText = "Please Confirm your account for the INTEGRIDY SYSTEM!.";
//        return confirmLink;
        return        header
                + "<p>Hello " + currentUser.getFirstName() + " " + currentUser.getLastName() + ",</p>"
                + "<br/>"
                + "<p>Welcome to the INTEGRIDY SYSTEM. </p>"
                + "<p>Please click on the following link in order to activate your Integridy account:</p>"
                + "<p><a href=\"" +confirmLink +"\" > Activate now </a></p>"
                + "<p>Link doesn't work? Copy the following link to your browser bar: </p>"
                + "<p><a href\""+ confirmLink +"\"> "+confirmLink+"</a></p>"
                + "<p>Have a nice day, </p>"
                 + "<p>Integridy Team </p>"
                + footerNormal;
    }
//    public static String userRegistrationHtmlExpert(User user, String newsLetterCheck) {
//        String confirmLink = BASE_URI + "/faces/account_confirmation.xhtml?apptoken=" + user.getToken();
//        String newsLetterText = "I see you also subscribed to our newsletter. ";
//        String expertText = "We are currently reviewing your application for the LIV_IN Virtual Expert Community and will get back to you soon.";
//        return header
//                + "<p>Hello " + user.getFirstName() + " " + user.getLastName() + ",</p>"
//                + "<br/>"
//                + "<p>A warm welcome to the LIV_IN platform. My name is Heike and I am your Community Manager.</p>"
//                + "<p>We are very happy you are here. We created the LIV_IN platform to keep you updated on the LIV_IN project and on the latest news about Smart Homes and Smart Health.</p>"
//                + "<p>" + (newsLetterCheck.equals("true") ? newsLetterText : "") + "Only one more step missing. Please confirm your subscription by clicking " + "<a href=" + confirmLink + ">here</a>.</p>"
//                + (user.getMotivation() != null ? "<p>" + expertText + "</p>" : "")
//                + "<p>If you have any questions do not hesitate to reply to this email and contact me.</p>"
//                + "<br/>"
//                + "<p>Have a nice day,</p>"
//                + footerWSignature;
//    }
//
//    public static String userRegistrationHtmlBasic(User user, String newsLetterCheck) {
//        String confirmLink = BASE_URI + "/faces/account_confirmation.xhtml?apptoken=" + user.getToken();
//        String newsLetterText = "I see you also subscribed to our newsletter. ";
//        return header
//                + "<p>Hello " + user.getFirstName() + " " + user.getLastName() + ",</p>"
//                + "<br>"
//                + "<p>A warm welcome to the LIV_IN platform. My name is Heike and I am your Community Manager.</p>"
//                + "<p>We are very happy you are here. We created the LIV_IN platform to keep you updated on the LIV_IN project and on the latest news about Smart Homes and Smart Health.</p>"
//                + "<p>" + (newsLetterCheck.equals("true") ? newsLetterText : "") + "Only one more step missing. Please confirm your subscription by clicking " + "<a href=" + confirmLink + ">here</a>.</p>"
//                + "<p>If you have any questions do not hesitate to reply to this email and contact me.</p>"
//                + "<br>"
//                + "<p>Have a nice day,</p>"
//                + footerWSignature;
//    }
//
//    public static String expertApplicationCm(UserApplication userApp) {
//        String confirmLink = BASE_URI + "/faces/cm/review.xhtml?apptoken=" + userApp.getUserId().getToken();
//        String newsLetterText = "I see you also subscribed to our newsletter. ";
//        String expertText = "We are currently reviewing your application for the LIV_IN Virtual Expert Community and will get back to you soon.";
//        return header
//                + "<p>Dear LIV_IN Community Manager,</p>"
//                + "<br>"
//                + "<p>" + userApp.getUserId().getFirstName() + " " + userApp.getUserId().getLastName() + " would like to join the LIV_IN Virtual Expert Community.</p>"
//                + "<p>Please review the following application: <a href=" + confirmLink + ">click here</a></p>"
//                + "<p>Motivation: " + userApp.getUserId().getMotivation() + "</p>"
//                + "<p>If you have any questions do not hesitate to reply to this email and contact me.</p>"
//                + "<br>"
//                + "<p>Thank you,</p>"
//                + footerWSignature;
//    }
//
//
////    public static String kuInvitationText(String userEmail, KnowledgeUnit ku,String message){
////        String invLink = BASE_URI+ "/faces/knowledgeUnit/individual-knowledge.xhtml?apptoken="+ku.getToken();
////        return "This article was sent to you by " + userEmail + " with the following message: \n"
////                + message + "\n"
////                +"<a href=\"" + invLink + "\">"+ ku.getTitle() +"</a>"
////                + "LIV_IN Virtual Community ";
////    }
//
//    public static String kuInvitationHtml(String mailReciver,User currentUser,KnowledgeUnit ku, String message) {
//        String invLink = BASE_URI + "/faces/news/individual-news.xhtml?apptoken=" + ku.getToken();
//        return header
//                + "<p>Hello,</p>"
//                + "<p>" + currentUser.getFirstName() + " " + currentUser.getLastName() + "(<a href=mailto:" + currentUser.getEmail() + ">" + currentUser.getEmail() + "</a>) is an international expert in the LIV_IN Virtual Expert Community. " + currentUser.getFirstName() + " asked us to share the following knowledge unit with you: "
//                + "<p>" + ku.getTitle() + "<br/>" +  ku.getKuShortDescription() + "</p>"
//                + (message != null ? "<p>" + message + "</p>" : "")
//                + "<p>If you would like to access it, simply click <a href=\"" +  invLink + "\">here</a></p>"
//                + "<p>My name is Heike and I am the Community Manager of the LIV_IN Virtual Expert Community.</p>"
//                + "<p>If you have any questions do not hesitate to reply to this email and contact me.</p>"
//                + "<p>Enjoy the article,</p>"
//                + footerWSignature;
//    }
//
//    //    public static String newsInvitationText(String userEmail, News news,String message){
////        String invLink = BASE_URI+ "/faces/news/individual-news.xhtml?apptoken="+news.getToken();
////        return "This article was sent to you by " + userEmail + " with the following message: \n"
////                + message + "\n"
////                +"<a href=\"" + invLink + "\">"+ news.getTitle() +"</a>"
////                + "LIV_IN Virtual Community ";
////    }
//    public static String newsInvitationHtml(User currentUser, News news, String message) {
//        String invLink = BASE_URI + "/faces/news/individual-news.xhtml?apptoken=" + news.getToken();
//        return header
//                + "<p>Hello,</p>"
//                + "<p>" + currentUser.getFirstName() + " " + currentUser.getLastName() + "(<a href=mailto:" + currentUser.getEmail() + ">" + currentUser.getEmail() + "</a>) asked us to share the following news article with you: "
//                + "<p>" + news.getTitle() + "<br/>" +  news.getNewsShortDescription() + "</p>"
//                + (message != null ? "<p>" + message + "</p>" : "")
//                + "<p>If you would like to access it, simply click <a href=\"" +  invLink + "\">here</a></p>"
//                + "<p>My name is Heike and I am the Community Manager of the LIV_IN Virtual Expert Community.</p>"
//                + "<p>If you have any questions do not hesitate to reply to this email and contact me.</p>"
//                + "<p>Enjoy the article,</p>"
//                + footerWSignature;
//    }
//
//
//    public static String passwordRecoveryHtml(ResetPassword resetPassword) {
//        String resetLink = BASE_URI + "/faces/reset-password.xhtml?apptoken=" + resetPassword.getToken();
//        return header
//                + "<p>Hello " + resetPassword.getUserId().getFirstName()+ "</p>"
//                + "<br>"
//                + "<p>Someone has requested a password reset for the following account: </p>"
//                + "<p>Username: " + resetPassword.getUserId().getName() + "</p>"
//                + "<p>If this was a mistake, please just ignore this email and nothing will happen.</p>"
//                + "<p>To reset your password, visit the following address: </p>"
//                + "<p>" + resetLink + "</p>"
//                + "<p>If you have any questions,do not hesitate to reply to this email and contact me. <br/>Have a nice day,</p>"
//                + footerWSignature;
//    }

//    public static String eventInvitation(String userEmail, Event event , String message){
//        String invLink = BASE_URI+ "/faces/event/individual-event.xhtml?apptoken="+ event.getToken();
//        return "This article was sent to you by " + userEmail + "with the following message: \n"
//                + message + "\n"
//                + "\t" + invLink + "\n\n"
//                +"<a href=\"" + invLink + "\">"+ event.getTitle() +"</a>"
//                + "LIV_IN Virtual Community ";
//    }



//    public static String registrationGeneric(String fullName){
//        return "Dear " + fullName + "," + "\n\n"
//                + "Thank you for your registration at LIV_IN  Virtual Community.\n"
//                + "Your application will be reviewed by the LIV_IN Community Manager as soon as possible.\n"
//                + "We will keep you informed of its approval status.\n"
//                + "Enjoy the LIV_IN platform!" + "\n\n"
//                + "LIV_IN Community Manager";
//    }
//    public static String newsGeneric(User currentUser, News news) {
//        return  header
//                + "<p>Hello " + currentUser.getFirstName() + ",</p>" + "\n\n"
//                + "<p>Congratulations! Your news article was successfully submitted!<br/>\n"
//                + "A Community Manager is currently reviewing your article and will get back to you soon.<br/>\n"
//                + "If you have any questions do not hesitate to reply to this email and contact me.<br/><p>" + "\n"
//                + "<p>Have a nice day,</p>" + "\n"
//                + footerWSignature;
//    }
//
//
//    public static String kuGeneric(User currentUser, KnowledgeUnit ku) {
//        return  header
//                + "<p>Hello " + currentUser.getFirstName() + ",</p>" + "\n\n"
//                + "<p>Congratulations! Your news Knowledge Unit was successfully submitted!<br/>\n"
//                + "A Community Manager is currently reviewing your Knowledge Unit and will get back to you soon.<br/>\n"
//                + "If you have any questions do not hesitate to reply to this email and contact me.<br/><p>" + "\n"
//                + "<p>Have a nice day,</p>" + "\n"
//                + footerWSignature;
//    }


    /**
     * Build an e-mail to inform community managers about user applications.
     *
     * @param currentUser             - current applicaton user
     * @param userKuApplication - Application token (for community managers to access and review user application)
     * @return mailMessageBody
     */

//    public static String kuGenericCm(User currentUser, UserKuApplication userKuApplication) {
//        String viewKuApplicationAdress = BASE_URI + "/faces/cm/reviewKu.xhtml?apptoken=" + userKuApplication.getToken();
//        return  header
//                + "<p>Hello Community Managers,</p>" + "\n\n"
//                + "<p>" + currentUser.getFirstName() + " " + currentUser.getLastName() + " submitted a Knowledge Unit.<br/>" + "\n\n"
//                + "Please review the Knowledge Unit <a href=\"" + viewKuApplicationAdress + "\">here</a></p>\n\n"
//                + "<p>If you have any questions do not hesitate to reply to this email and contact me. <br/>\n"
//                + "Have a nice day,</p> \n"
//                + footerWSignature;
//    }
//
//    public static String newsGenericCm(User currentUser, UserNewsApplication userNewsApplication) {
//        String viewNewsApp = BASE_URI + "/faces/cm/reviewNews.xhtml?apptoken=" + userNewsApplication.getToken();
//        return  header
//                + "<p>Hello Community Managers,</p>" + "\n\n"
//                + "<p>" + currentUser.getFirstName() + " " + currentUser.getLastName() + " submitted a news article.<br/>" + "\n\n"
//                + "Please review the news article <a href=\"" + viewNewsApp + "\">here</a></p>\n\n"
//                + "<p>If you have any questions do not hesitate to reply to this email and contact me. <br/>\n"
//                + "Have a nice day,</p> \n"
//                + footerWSignature;
//    }

    //    public static String kuGenericCm(String user, String applicationToken) {
//        String viewKuApplicationAdress = BASE_URI + "/faces/cm/reviewKu.xhtml?apptoken=" + applicationToken;
//        return "Dear LIV_IN Community Manager," + "\n\n"
//                + user + " would like to publish a new Knowledge Unit for the LIV_IN platform. " + "\n\n"
//                + "Please, review the application here:  " + viewKuApplicationAdress + "\n\n"
//                + "Thank you, \n"
//                + "LIV_IN  Virtual Community";
//    }
//    public static String registrationGenericCm(String user, String roleAppliedFor, String applicationToken){
//        String viewApplicationAddress = BASE_URI + "/faces/cm/review.xhtml?apptoken=" + applicationToken;
//        return "Dear LIV_IN Community Manager," + "\n\n"
//                + "A new user " + user + " has applied with a role of " + roleAppliedFor + " for the LIV_IN  Virtual Community" + "\n\n"
//                + "Please, review the application here: " + viewApplicationAddress + "\n\n"
//                + "Thank you, \n"
//                + "LIV_IN  Virtual Community";
//    }
//    public static String expertApplicationCm(String user, String applicationToken){
//        String viewApplicationAddress = BASE_URI + "/faces/cm/review.xhtml?apptoken=" + applicationToken;
//        return "Dear LIV_IN Community Manager," + "\n\n"
//                + user + " has applied for Expert Role on LIV_IN  Virtual Community" + "\n\n"
//                + "Please, review the application here: " + viewApplicationAddress + "\n\n"
//                + "Thank you, \n"
//                + "LIV_IN  Virtual Community";
//    }


    /**
     * Build an e-mail to inform users about their rejected applications.
     *
     * @param userApplication - user full name
     * @return mailMessageBody
     */
//    public static String registrationRefusalEmail(UserApplication userApplication) {
//        String loginUrl = BASE_URI + "/faces/login.xhtml";
//        return  header
//                + "<p>Hello " + userApplication.getUserId().getFirstName() + "," + "</p>\n\n"
//                + "<p>Thank you for your application as an expert. In the interest of a balanced composition of the LIV_IN Virtual Expert Community, we decided to put your application on hold for now. We will keep your application active and will inform you as soon as circumstances change." +
//                " However, you can still access the LIV_IN platform (including the News) and sign in with your user name and password <a href=\"" + loginUrl + "\">here</a></p>" + "\n"
//                + "<p>If you have any questions do not hesitate to reply to this email and contact me.</p>"
//                + "<br/><br/><p>Enjoy your stay,</p>" + "\n"
//                +footerWSignature;
//    }

//    public static String kuRefusalEmail(String userFullName, String rejectReason) {
//        return "Dear " + userFullName + "," + "\n\n"
//                + "Thank you for your Knowledge Unit submission at LIV_IN  Virtual Community." + "\n"
//                + "Unfortunately your application has been rejected." + "\n"
//                + "Rejection reason: \"" + rejectReason + "\"\n\n"
//                + "Enjoy the LIV_IN platform!" + "\n\n"
//                + "LIV_IN Community Manager";
//    }

//    public static String kuRefusalEmail(UserKuApplication userKuApplication, String rejectReason) {
//        return  header
//                + "<p>Hello " + userKuApplication.getUserId().getFirstName() + ",</p>" + "\n\n"
//                + "<p>Unfortunately, your Knowledge Unit was rejected.<br/>"
//                + "<p>However, if you still make some changes you can submit the revised Knowledge Unit. <br/>"
//                + "<p>Therefore, please have a look at the following review of your Knowledge Unit. </p>"
//                + "<p>" + rejectReason + "</p>."
//                + "<p>If you have any questions do not hesitate to reply to this email and contact me.<br/>" + "\n"
//                + "Have a nice day,</p>" + "\n\n"
//                + footerWSignature;
//    }
//
//    public static String newsRefusalEmail(UserNewsApplication userNewsApplication, String rejectReason) {
//        return  header
//                + "<p>Hello " + userNewsApplication.getUserId().getFirstName() + ",</p>" + "\n\n"
//                + "<p>Unfortunately, your news article was rejected.<br/>"
//                + "<p>However, if you still make some changes you can submit the revised article. <br/>"
//                + "<p>Therefore, please have a look at the following review of your article. </p>"
//                + "<p>" + rejectReason + "</p>."
//                + "<p>If you have any questions do not hesitate to reply to this email and contact me.<br/>" + "\n"
//                + "Have a nice day,</p>" + "\n\n"
//                + footerWSignature;
//    }

//    /**
//     * Build an e-mail to inform users about their approved applications.
//     *
//     * @param userApplication - user name
//     * @return mailMessageBody
//     */
//    public static String userApproveEmail(UserApplication userApplication ) {
//        String loginUrl = BASE_URI + "/faces/login.xhtml";
//        return  header +
//                "Hello " + userApplication.getUserId().getFirstName() + "," + "\n\n"
//                + "<p>Congratulations! Your application was approved! We are excited to have you with us!</p>" + "\n"
//                + "<p>We created the LIV_IN Virtual Expert Community to offer a trustworthy, inspiring and innovative space for you to share knowledge, get in touch with fellow experts and jointly work on the Smart Homes and Smart Health innovations of 2030. </p>" + "\n"
//                + "<p>Come in and experience the LIV_IN Virtual Expert Community <a href=\"" + loginUrl + "\">here</a></p>\n"
//                + "<p>If you have any questions do not hesitate to reply to this email and contact me.</p>"
//                + "<br/><br/><p>Enjoy your stay,</p>" + "\n"
//                +footerWSignature;
//    }
//
//    public static String kuApproveEmail(UserKuApplication userKuApplication) {
//        String kuUrl = BASE_URI + "/faces/knowledgeUnit/individual-knowledge.xhtml?apptoken=" + userKuApplication.getToken();
//        return  header
//                + "<p>Hello " + userKuApplication.getUserId().getFirstName() + ",</p>" + "\n\n"
//                + "<p>Congratulations! Your Knowledge Unit was approved!<br/>" + userKuApplication.getKuId().getTitle()
//                + " You can access the Knowledge Unit <a href=\"" + kuUrl + "\">here</a><p>." + "\n"
//                + "<p>If you have any questions do not hesitate to reply to this email and contact me.<br/>" + "\n"
//                + "Enjoy the article,</p>" + "\n\n"
//                + footerWSignature;
//    }
//
//    public static String newsApproveEmail(UserNewsApplication userNewsApplication) {
//        String newsUrl = BASE_URI + "/faces/news/individual-news.xhtml?apptoken=" + userNewsApplication.getToken();
//        return  header
//                + "<p>Hello " + userNewsApplication.getUserId().getFirstName() + ",</p>" + "\n\n"
//                + "<p>Congratulations! Your news article was approved!<br/>" + userNewsApplication.getNewsId().getTitle()
//                + " You can access the article <a href=\"" + newsUrl + "\">here</a><p>." + "\n"
//                + "<p>If you have any questions do not hesitate to reply to this email and contact me.<br/>" + "\n"
//                + "Enjoy the article,</p>" + "\n\n"
//                + footerWSignature;
//    }
//
////    public static String kuApproveEmail(String userFullName, KnowledgeUnit ku) {
////        String kuUrl = BASE_URI + "/faces/knowledgeUnit/individual-knowledge.xhtml?apptoken=" + ku.getToken();
////        return "Dear " + userFullName + "," + "\n\n"
////                + "Congratulations! After careful review, your Knowledge Unit " + ku.getTitle()
////                + " has been approved. It will now be published and accessible on the platform on this link " + kuUrl + "." + "\n"
////                + "We look forward to more contributions from your side! Thank you!" + "\n"
////                + "Enjoy the LIV_IN platform!" + "\n\n"
////                + "LIV_IN Community Manager";
////    }
//
//
//
//
//
//
//    /**
//     * Build an e-mail to inform users about their applications.
//     *
//     * @param userKuApplication User full name
//     * @param reviseReason Revise reason for user
//     * @return mailMessageBody
//     */
//
//    public static String kuRevisionEmail(UserKuApplication userKuApplication,String reviseReason) {
//        String revisionKuUrl = BASE_URI + "/faces/cm/revisionKu.xhtml?apptoken=" + userKuApplication.getKuId().getToken();
//        return  header
//                + "<p>Hello " + userKuApplication.getUserId().getFirstName() + ",</p>" + "\n\n"
//                + "<p>There are certain amendments needed before publishing your Knowledge Unit " + userKuApplication.getKuId().getTitle() + ":</p>"
//                + "<p>" + reviseReason + "</p>"
//                + "<p>To edit your application please follow the link " + revisionKuUrl + "</p>"
//                + "<p>As soon as the necessary amendments are done, you Knowledge Unit will be published and accessible on the platform.</p>"
//                + "<p>We look forward to more contributions from your side! Thank you!</p>"
//                + "<p>Enjoy the LIV_IN platform,</p>"
//                + footerWSignature;
//    }
//
////    public static String kuRevisionEmail(String userFullName, String reviseReason, KnowledgeUnit ku) {
////        String revisionKuUrl = BASE_URI + "/faces/cm/revisionKu.xhtml?apptoken=" + ku.getToken();
////        return "Dear " + userFullName + " , " + "\n\n"
////                + "There are certain amendments needed before publishing your Knowledge Unit " + ku.getTitle() + ":\n\n"
////                + reviseReason + "\n\n"
////                + "To edit your application please follow the link " + revisionKuUrl + "\n"
////                + "As soon as the necessary amendments are done, you Knowledge Unit will be published and accessible on the platform.\n"
////                + "We look forward to more contributions from your side! Thank you!\n"
////                + "Enjoy the LIV_IN platform!" + "\n\n"
////                + "LIV_IN Community Manager";
////    }
//
////    public static String getUserRevisionMesage(String fullname, String applicationToken){
////        String reviewKu = BASE_URI+ "/faces/cm/revisionKu.xhtml?apptoken=" + applicationToken;
////        return "Dear Livin " + fullname + "," + "\n\n"
////                + "Your Knowledge Unit application needs a revision "  + "\n\n"
////                + "Please review the application by visiting: " + reviewKu + "\n\n"
////                + "Thank you, \n"
////                + "Livin - Virtual Community Platform";
////    }
//
//    public static String contactFormMessage(String firstName, String lastName, String emailAddress, String message, String subject, String affiliation) {
//        return "A new contact form was submitted." + "\n\n"
//                + "First Name: " + firstName + "\n"
//                + "Last Name: " + lastName + "\n"
//                + "Email address: " + emailAddress + "\n"
//                + "Affiliation: " + affiliation + "\n"
//                + "Subject: " + subject + "\n"
//                + "Message: " + message + "\n\n"
//                + "Sincerely, \n"
//                + "LIV_IN  Virtual Community";
//    }

}
