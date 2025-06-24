<%-- 
    Document   : uploader
    Author     : Victor Ojeda and Pablo Pascual
--%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>JLINK Uploader</title>
            <link rel="stylesheet" type="text/css" href=<%=jlink.Const.AppConst.CSS_SYLE_PATH %> />
        </head>

        <body>
            <div class="header">
                <div class="header_left">
                    <h1>JLINK APPLICATION | Uploader</h1>
                </div>
                <div class="header_right">
                    <img class="header_image" src=<%=jlink.Const.AppConst.HEADER_LOGO_PATH %> />
                </div>
            </div>
            <div class="main_container">
                <div class="title">
                    <h1>Upload a JLINK file</h1>
                </div>
                <div class="container">
                    <div class="column">
                        <table>
                            <form method="post" action="Uploader" , enctype="multipart/form-data">
                                <tr>
                                    <td>File information: </td>
                                </tr>
                                <tr>
                                    <td>JLINK File Title: </td>
                                    <td><input type="text" name="file_title" /></td>
                                </tr>
                                <tr>
                                    <td>JLINK File Description: </td>
                                    <td><textarea name="file_description" /></textarea></td>
                                </tr>
                                <tr>
                                    <td>Upload file: </td>
                                    <td><input type="file" name="image" id="imgInp" required /></td>
                                </tr>
                                <tr>
                                    <td><input type="submit" value="Next" /></td>
                                </tr>
                            </form>
                        </table>
                    </div>
                    <div class="column">
                        <img class="image" id="pre-img" src="#" />
                    </div>
                </div>
            </div>
            <script type="text/javascript" src=<%=jlink.Const.AppConst.JS_PREVIEW_IMAGE_PATH %>></script>
        </body>

        </html>