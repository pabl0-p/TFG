package jlink.Const;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Victor Ojeda and Pablo Pascual
 */
public class AppConst {

    // public static final String IMAGES_DATABASE_CONNECTION = "jdbc:postgresql://psql-server:5432/JLINK_APP?user=postgres&password=postgres";
    public static final String DATABASE_CONNECTION = "jdbc:postgresql://psql-server:5432/%s?user=%s&password=%s";
    public static final String KEYDB_CONNECTION = "jdbc:postgresql://psql-key-server:5432/%s?user=%s&password=%s";

    public static final String CREATOR_DIR = "creator";
    public static final String SAVE_DIR = "uploadFiles";
    public static final String VIEW_DIR = "viewer";
    public static final String MODIFIER_DIR = "modifier";
    public static final String UPLOADER_DIR = "uploader";

    public static final String CSS_SYLE_PATH = "css/style.css";

    public static final String JS_PREVIEW_IMAGE_PATH = "js/preview-image.js";
    public static final String JS_SELECT_SPRITE_PATH = "js/select-sprite.js";
    public static final String JS_TABLE_VIEW_PATH = "js/table-view.js";
    public static final String JS_CONTAIN_TABLE_PATH = "js/contain-table.js";
    public static final String JS_CONTAIN_TABLE_MODIFIER_PATH = "js/contain-table-modifier.js";
    public static final String JS_FINISH_RESULT_PATH = "js/finish-result.js";
    public static final String JS_VIEWER_PATH = "viewer/viewer.js";
    public static final String JS_DOWNLOAD_FILE_PATH = "js/download-file.js";
    public static final String JS_DELETE_FILE_PATH = "js/delete-confirm.js";
    public static final String JS_PRIVSEC_SELECT_PATH = "js/privsec-selection.js";
    public static final String JS_ROI_SELECT_PATH = "js/roi-selection.js";

    // https://ilabrs.upc.edu/ca/logos/logo-etsetb/view
    public static final String HEADER_LOGO_PATH = "appImages/Logo_ETSETB.png";
    // https://uxwing.com/back-button-icon/
    public static final String BACK_BUTTON_PATH = "appImages/back-button.svg";

    public static final String SPRITE_PATH = "appImages/sprite.jpeg";
    public static final String SPRITE_BLUE_PATH = "appImages/sprite_blue.jpeg";
    public static final String SPRITE_RED_PATH = "appImages/sprite_red.jpeg";

    public static final String PROTECTED_CONTENT_PATH = "/images/protected_content.jpeg";
    public static final String ACCESS_CONTENT_PATH = "/images/access_denied.jpeg";

}
