/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.NodeSelectEvent;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import ro.siveco.cad.integridy.entities.ConsumptionPoint;
import ro.siveco.cad.integridy.entities.SmartMeter;
import ro.siveco.cad.integridy.entities.Users;

/**
 *
 * @author roxanam
 */
@Named("myAccountController")
@ViewScoped
public class MyAccountController implements Serializable {
    @EJB
    private DashboardService dashboardService;
    @EJB
    private UsersFacade usersFacade;
    @EJB
    private ConsumptionPointFacade consumptionPointFacade;
    @EJB
    private SmartMeterFacade smartMeterFacade;
    
    
    private ConsumptionPoint selectedConsumptionPoint;
    private SmartMeter selectedSmartMeter;
    
    private Users currentUser;
    
    private String oldPassword;
    private String newPassword1;
    private String newPassword2;
    
    private boolean changePassword;
    
    
    private TreeNode root;
    
    private TreeNode selectedNode;
    
    
    
    @PostConstruct
    public void init(){
        currentUser = usersFacade.findByUserName(dashboardService.getUsername());
        root = createTree();
    }
    public TreeNode createTree(){
        if(dashboardService.getCurrentConsumerClient()==null)
            return null;
        TreeNode root = new DefaultTreeNode(new DevicesNode(0,"","root","root"), null);
        List<ConsumptionPoint> consumptionPointList = consumptionPointFacade.getByClientId(dashboardService.getCurrentConsumerClient().getId());
        for(ConsumptionPoint cp:consumptionPointList){
            TreeNode cpNode = new DefaultTreeNode("CP",new DevicesNode(cp.getId(), cp.getPointType(),"CP" , cp.getPointName()) ,root);
            List<SmartMeter> smartMeterList = smartMeterFacade.getByConsumptionPointId(cp.getId());
            for(SmartMeter sm : smartMeterList){
                TreeNode smNode = new DefaultTreeNode("SM",new DevicesNode(sm.getId(),sm.getDeviceType(), "SM" , sm.getDeviceName()) ,cpNode);
            }
        }
         return root;
    }
   
    public void onNodeSelect(NodeSelectEvent event) {
        
        DevicesNode nodeData = (DevicesNode)event.getTreeNode().getData();
        selectedConsumptionPoint = null;
        selectedSmartMeter = null;
        if("SM".equals(nodeData.getType())){
            selectedSmartMeter = smartMeterFacade.find(nodeData.getNodeId());
        }else if("CP".equals(nodeData.getType())){
            selectedConsumptionPoint = consumptionPointFacade.find(nodeData.getNodeId());
        }
        
    }
 
   
    public void updateAccountData(){
        if(changePassword){
             //check input is not empty
            if(oldPassword.trim().isEmpty() | newPassword1.trim().isEmpty() || newPassword2.trim().isEmpty()){
                FacesMessage invalidInput = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Input fields cannot be empty", "Please fill all the required fields");
                FacesContext.getCurrentInstance().addMessage(null, invalidInput);
                return;
            }
        
               //check old password
            if(!currentUser.getPassword().equals(oldPassword)){
                FacesMessage oldPasswordIncorrect = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Old password incorrect", "");
                FacesContext.getCurrentInstance().addMessage(null, oldPasswordIncorrect);
                return;
            }
        
            //check new password confirmation
            if(!newPassword1.equals(newPassword2)){
                FacesMessage passwordsDoNotMatch = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match!","");
                FacesContext.getCurrentInstance().addMessage(null, passwordsDoNotMatch);
                return;
            }
        
            //check if new password is equal to old password
            if(newPassword1.equals(oldPassword)){
                FacesMessage oldPasswordDif = new FacesMessage(FacesMessage.SEVERITY_ERROR, "New password cannot be the same as your old password", "");
                FacesContext.getCurrentInstance().addMessage(null, oldPasswordDif);
                return;
            }
        
            //change password
            try{
                currentUser.setPassword(newPassword1);
                usersFacade.edit(currentUser);
                FacesMessage changePasswordSuccess = new FacesMessage(FacesMessage.SEVERITY_INFO, "Password changed successfully!",
                        "Your password has been changed successfully!");
                FacesContext.getCurrentInstance().addMessage(null, changePasswordSuccess);
                }catch(Exception ex){
                     Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                     FacesMessage updateAccountError = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Account data was not updated!",
                        "Account data was not updated!");
                    FacesContext.getCurrentInstance().addMessage(null, updateAccountError);
                }
                
        }else{
                try{
            usersFacade.edit(currentUser);
            FacesMessage updateAccountSuccess = new FacesMessage(FacesMessage.SEVERITY_INFO, "Account data changed successfuly!",
                        "Account data changed successfuly!");
            FacesContext.getCurrentInstance().addMessage(null, updateAccountSuccess);
            }catch(Exception ex){
                     Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                      FacesMessage updateAccountError = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Account data was not updated!",
                        "Account data was not updated!");
                    FacesContext.getCurrentInstance().addMessage(null, updateAccountError);
                }
                
        }
        
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }
    
    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword1() {
        return newPassword1;
    }

    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public ConsumptionPoint getSelectedConsumptionPoint() {
        return selectedConsumptionPoint;
    }

    public void setSelectedConsumptionPoint(ConsumptionPoint selectedConsumptionPoint) {
        this.selectedConsumptionPoint = selectedConsumptionPoint;
    }

    public SmartMeter getSelectedSmartMeter() {
        return selectedSmartMeter;
    }

    public void setSelectedSmartMeter(SmartMeter selectedSmartMeter) {
        this.selectedSmartMeter = selectedSmartMeter;
    }

    
    
    
}
