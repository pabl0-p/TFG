<%-- 
    Document   : creator
    Author     : Victor Ojeda and Pablo Pascual
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JLINK Creator</title>
        <link rel="stylesheet" type="text/css" href=<%= jlink.Const.AppConst.CSS_SYLE_PATH %> />    
    </head>
    <body>
        <div class="header">
            <div class="header_left">
                <h1>JLINK APPLICATION | Creator</h1>
            </div>
            <div class="header_right">
                <img class="header_image" src=<%= jlink.Const.AppConst.HEADER_LOGO_PATH %> />
            </div>
        </div>
        <div class="main_container">          
            <div class="title">
                <h1>Create a JLINK file</h1>
            </div>
            <div class="container">
                <div class="column">
                    <form method="post" action="AddReplacement", enctype = "multipart/form-data">
                        <table>
                        <tr><td>File information: </td></tr>
                        <tr><td>File name: </td><td><input type="text" name="file_name"  required/></td></tr>
                        <tr><td>JLINK File Title: </td><td><input type="text" name="file_title"/></td></tr>
                        <tr><td>JLINK File Description: </td><td><textarea name="file_description"/></textarea></td></tr>
                        <tr><td></br>Add first image: </td></tr>
                        <tr><td>Upload image: </td><td><input type="file" name="image" id="imgInp" required/></td></tr>
                        <tr><td>Title: </td><td><input type="text" name="title" required/></td></tr>
                        <tr><td>Description: </td><td><textarea name="description"/></textarea></td></tr>
                        <tr><td>Encryption: </td>
                            <td><select name="encryption" id="encryption" >
                                <option value=""></option>
                                <option value= "AES256">AES 256</option>
                                <option value= "AES256IV">AES 256 with IV</option>
                            </select></td></tr>
                        <tr><td>Replacement: </td>
                            <td><select name="replacement" id="replacement">
                                <option value=""></option>
                                <option value= "roi">ROI</option>
                                <option value= "replace_img">Replacement image</option>
                            </select></td></tr>
                        <tr><td>Access (Ctrl+Click): </td>
                            <td>View<br>
                            <select name="view_access" id="view_access" multiple size=3>
                                <option value= "admin">Admin</option>
                                <option value= "user">User</option>
                                <option value= "guest">Guest</option>
                            </select></td></tr>
                        <tr><td></td><td>Edit<br>
                            <select name="edit" id="edit" multiple size=3>
                            <option value= "admin">Admin</option>
                            <option value= "user">User</option>
                            <option value= "guest">Guest</option>
                        </select></td></tr>    
                        <tr><td><input type="submit" value="Next" /></td></tr>    
                    </table></form>
                </div>
                <div class="column">
                    <img class="image" id="pre-img" src="#"/>
                </div>
            </div>
        </div>
        <script type="text/javascript" src=<%= jlink.Const.AppConst.JS_PREVIEW_IMAGE_PATH %>></script>
        <script type="text/javascript" src=<%= jlink.Const.AppConst.JS_PRIVSEC_SELECT_PATH %>></script>
    </body>
</html>
