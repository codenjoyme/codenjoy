<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Upload application</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/jquery.fileupload-ui.css" rel="stylesheet">
</head>

<body>
<%--<script src="${pageContext.request.contextPath}/resources/js/jquery-1.8.2.min.js"></script>--%>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.8.2.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/vendor/jquery.ui.widget.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.iframe-transport.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.fileupload.js"></script>

<%--<input id="fileupload" type="file" name="files[]" data-url="server/php/" multiple>--%>

<form id="fileupload" action="${pageContext.request.contextPath}/upload" method="post"
      enctype="multipart/form-data">

    <div class="row fileupload-buttonbar">
        <div class="span7">
            <span class="btn btn-primary fileinput-button">
                    <i class="icon-upload icon-white"></i>
                    <span>Run ...</span>
                    <input type="file" name="files[]">
            </span>
            <%--<button type="submit" class="btn btn-primary start">
                <i class="icon-upload icon-white"></i>
                <span>Upload</span>
            </button>--%>
            <div class="span5 fade" id="infoPanel">
                <div id="uploadLabel" class="text-info">No file selected...</div>
                <div class="progress progress-striped active" id="progressbar" aria-valuemin="0" aria-valuemax="100">
                    <div class="bar" style="width: 0%;"></div>
                </div>
            </div>
        </div>
    </div>
</form>

<script>
    function showProgress(progress) {
        $('#progressbar .bar').css(
                'width',
                progress + '%'
        );
    }
    $(function () {
        $('#fileupload').fileupload({
            dataType:'json',
            add:function (e, data) {
                $('#infoPanel').removeClass('fade');
                $('#infoPanel').addClass('fade.in');
                $('#uploadLabel').text('Uploading ' + data.files[0].name + " ...");
                data.submit()
            },
            done:function (e, data) {
                $.each(data.result, function (key, file) {
                    $('#uploadLabel').text('Running ' + file.fileName + " ...");
                });
            },
            progressall:function (e, data) {
                showProgress(parseInt(data.loaded / data.total * 100, 10));
            },
            progres:function (e, data) {
                showProgress(parseInt(data.loaded / data.total * 100, 10));
                $('#console').text('progress ' + progress);
            }
        });
    });
</script>
<div id="console"></div>
</body>
</html>
