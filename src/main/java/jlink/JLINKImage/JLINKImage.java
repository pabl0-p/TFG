/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jlink.JLINKImage;

import java.util.ArrayList;
import java.util.List;

import jlink.Const.AppConst;
import jlink.Const.JLINKConst;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Victor Ojeda and Pablo Pascual
 */
public class JLINKImage {

    private String mergeFileName;
    private String label;
    private int id;

    private String previous_image;
    private List<JLINKImage> linked_images;

    private String version, title, note;
    private String viewport_X, viewport_Y, viewport_xFov, viewport_yFov;
    private int viewport_Id;
    private String image_format, image_Href;
    private String link_duration, link_sprite, link_to;
    private int link_Vpid;
    private String link_region_name, link_region_shape;
    private String link_region_X, link_region_Y, link_region_W, link_region_H, link_region_rotation;
    private byte[] data;
    private String appPath;
    private String sprite_color;
    private String encryption, replacement;
    private String[] view_access, edit_access;
    private int roi_region_X, roi_region_Y, roi_width, roi_height;
    private String replacement_image;
    private IvParameterSpec ivSpec;
    private SecretKeySpec secretKey;
    
    private boolean isMain;
    private boolean possibleAction, decryptionPossible, change;

    public JLINKImage() {
        this.linked_images = new ArrayList<>();
        this.isMain = false;
        this.version = JLINKConst.DEFAULT_VERSION;
        this.sprite_color = AppConst.SPRITE_PATH;
        this.link_duration = JLINKConst.DEFAULT_LINK_DURATION;
    }

    public void addLink(JLINKImage img) {
        this.linked_images.add(img);
    }

    public void setMergeFileName(String mergeFileName) {
        this.mergeFileName = mergeFileName;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrevious_image(String previous_image) {
        this.previous_image = previous_image;
    }

    public void setLinked_images(List<JLINKImage> linked_images) {
        this.linked_images = linked_images;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setViewport_X(String viewport_X) {
        this.viewport_X = viewport_X;
    }

    public void setViewport_Y(String viewport_Y) {
        this.viewport_Y = viewport_Y;
    }

    public void setViewport_xFov(String viewport_xFov) {
        this.viewport_xFov = viewport_xFov;
    }

    public void setViewport_yFov(String viewport_yFov) {
        this.viewport_yFov = viewport_yFov;
    }

    public void setViewport_Id(int viewport_Id) {
        this.viewport_Id = viewport_Id;
    }

    public void setImage_format(String image_format) {
        this.image_format = image_format;
    }

    public void setImage_Href(String image_Href) {
        this.image_Href = image_Href;
    }

    public void setLink_duration(String link_duration) {
        this.link_duration = link_duration;
    }

    public void setLink_Vpid(int link_Vpid) {
        this.link_Vpid = link_Vpid;
    }

    public void setLink_sprite(String link_sprite) {
        this.link_sprite = link_sprite;
    }

    public void setLink_to(String link_to) {
        this.link_to = link_to;
    }

    public void setLink_region_name(String link_region_name) {
        this.link_region_name = link_region_name;
    }

    public void setLink_region_shape(String link_region_shape) {
        this.link_region_shape = link_region_shape;
    }

    public void setLink_region_X(String link_region_X) {
        this.link_region_X = link_region_X;
    }

    public void setLink_region_Y(String link_region_Y) {
        this.link_region_Y = link_region_Y;
    }

    public void setLink_region_W(String link_region_W) {
        this.link_region_W = link_region_W;
    }

    public void setLink_region_H(String link_region_H) {
        this.link_region_H = link_region_H;
    }

    public void setLink_region_rotation(String link_region_rotation) {
        this.link_region_rotation = link_region_rotation;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
    }

    public void setSprite_color(String sprite_color) {
        this.sprite_color = sprite_color;
    }

    public void setEncryption(String encryption){
        this.encryption = encryption;
    }
    
    public void setReplacement(String replacement){
        this.replacement = replacement;
    }

    public void setViewAccess(String[] view_access){
        this.view_access = view_access;
    }

    public void setEditAccess(String[] edit_access){
        this.edit_access = edit_access;
    }

    public void setROI_region_X(int roi_region_X){
        this.roi_region_X = roi_region_X;
    }

    public void setROI_region_Y(int roi_region_Y){
        this.roi_region_Y = roi_region_Y;
    }

    public void setROI_Width(int roi_width){
        this.roi_width = roi_width;
    }

    public void setROI_Height(int roi_height){
        this.roi_height = roi_height;
    }

    public void setReplacementImage(String replacement_image){
        this.replacement_image = replacement_image;
    }

    public void setSecretKey(SecretKeySpec secretKey){
        this.secretKey = secretKey;
    }

    public void setIVSpec(IvParameterSpec ivSpec){
        this.ivSpec = ivSpec;
    }

    public void setPossibleAction(Boolean possibleAction){
        this.possibleAction = possibleAction;
    }

    public void setDecryptionPossible(Boolean decryptionPossible){
        this.decryptionPossible = decryptionPossible;
    }

    public void setChange(Boolean change) {
        this.change = change; 
    }

    public String getMergeFileName() {
        return mergeFileName;
    }

    public String getLabel() {
        return label;
    }

    public int getId() {
        return id;
    }

    public String getPrevious_image() {
        return previous_image;
    }

    public List<JLINKImage> getLinked_images() {
        return linked_images;
    }

    public String getVersion() {
        return version;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public String getViewport_X() {
        return viewport_X;
    }

    public String getViewport_Y() {
        return viewport_Y;
    }

    public String getViewport_xFov() {
        return viewport_xFov;
    }

    public String getViewport_yFov() {
        return viewport_yFov;
    }

    public int getViewport_Id() {
        return viewport_Id;
    }

    public String getImage_format() {
        return image_format;
    }

    public String getImage_Href() {
        return image_Href;
    }

    public String getLink_duration() {
        return link_duration;
    }

    public int getLink_Vpid() {
        return link_Vpid;
    }

    public String getLink_sprite() {
        return link_sprite;
    }

    public String getLink_to() {
        return link_to;
    }

    public String getLink_region_name() {
        return link_region_name;
    }

    public String getLink_region_shape() {
        return link_region_shape;
    }

    public String getLink_region_X() {
        return link_region_X;
    }

    public String getLink_region_Y() {
        return link_region_Y;
    }

    public String getLink_region_W() {
        return link_region_W;
    }

    public String getLink_region_H() {
        return link_region_H;
    }

    public String getLink_region_rotation() {
        return link_region_rotation;
    }

    public byte[] getData() {
        return data;
    }

    public String getAppPath() {
        return appPath;
    }

    public boolean isIsMain() {
        return isMain;
    }

    public String getSprite_color() {
        return sprite_color;
    }

    public String getEncryption(){
        return encryption;
    }

    public String getReplacement(){
        return replacement;
    }

    public String[] getViewAccess(){
        return view_access;
    }

    public String[] getEditAccess(){
        return edit_access;
    }

    public int getROI_region_X(){
        return roi_region_X;
    }

    public int getROI_region_Y(){
        return roi_region_Y;
    }

    public int getROI_Width(){
        return roi_width;
    }

    public int getROI_Height(){
        return roi_height;
    }

    public String getReplacementImage(){
        return replacement_image;
    }

    public SecretKeySpec getSecretKey(){
        return secretKey;
    }

    public IvParameterSpec getIVSpec(){
        return ivSpec;
    }

    public Boolean getPossibleAction(){
        return possibleAction;
    }

    public Boolean getDecryptionPossible(){
        return decryptionPossible;
    }

    public Boolean getChange() {
        return change;
    }
}
