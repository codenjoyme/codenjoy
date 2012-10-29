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

<form id="fileupload" action="${pageContext.request.contextPath}/upload" method="post"
      enctype="multipart/form-data">
    <div id="error"></div>

    <div class="row fileupload-buttonbar">
        <div class="span7">
            <span class="btn btn-primary fileinput-button">
                    <i class="icon-upload icon-white"></i>
                    <span>Run ...</span>
                    <input type="file" name="files[]">
            </span>

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
    function startPollGameProgress(timestamp) {
        function poll() {
            $.ajax({ url:'${pageContext.request.contextPath}'+'/progress?timestamp=' + timestamp,
                success:function (data) {
                    if (data == null) {
                        $("#error").text("There is no progress available!");
                        return;
                    }
                    showProgress(data.progress);
                    if (data.progress >= 100) {
                        $("#uploadLabel").text("Game execution finished");
                    }else{
                        poll();
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $("#error").text("Error on getting game progress. status:"+xhr.status + " error: "+ thrownError);
                },
                dataType:"json", cache:false, timeout:30000 });
        }

        poll();
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
                    showProgress(0);
                    startPollGameProgress(file.timestamp);
                });
            },
            progressall:function (e, data) {
                showProgress(parseInt(data.loaded / data.total * 100, 10));
            },
            progres:function (e, data) {
                showProgress(parseInt(data.loaded / data.total * 100, 10));
            }
        });
    });
</script>

</body>
</html>
