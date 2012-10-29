<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;">
    <title>Upload application</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/jquery.fileupload-ui.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/DT_bootstrap.css" rel="stylesheet">
</head>

<body>
<%--<script src="${pageContext.request.contextPath}/resources/js/jquery-1.8.2.min.js"></script>--%>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.8.2.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/vendor/jquery.ui.widget.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.iframe-transport.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.fileupload.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.dataTables.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/DT_bootstrap.js"></script>

<div id="error"></div>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span4">

            <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered dataTable"
                   id="gamelogs" aria-describedby="gamelogs_info">
                <thead>
                <tr role="row">
                    <th class="sorting_asc" role="columnheader" tabindex="0" aria-controls="gamelogs" rowspan="1"
                        colspan="1"
                        aria-sort="ascending">
                        Played timestamp
                    </th>
                    <th class="sorting" role="columnheader" tabindex="0" aria-controls="gamelogs" rowspan="1"
                        colspan="1"
                        aria-label="Browser: activate to sort column ascending">
                        Replay
                    </th>
                </tr>
                </thead>

                <tbody role="alert" aria-live="polite" aria-relevant="all">
                <tr class="gradeA odd">
                    <td class="  sorting_1">Gecko</td>
                    <td class=" ">Firefox 1.0</td>
                </tr>
                <tr class="gradeA even">
                    <td class="  sorting_1">Gecko</td>
                    <td class=" ">Firefox 1.5</td>
                </tr>
                </tbody>
            </table>


            <form id="fileupload" action="${pageContext.request.contextPath}/uploadApp" method="post"
                  enctype="multipart/form-data">
                <div class="row fileupload-buttonbar span12">
                    <div class="span3">
                        <span class="btn btn-primary fileinput-button">
                                <i class="icon-upload icon-white"></i>
                                <span>Run ...</span>
                                <input type="file" name="files[]">
                        </span>
                    </div>

                    <div class="span9 fade" id="infoPanel">
                        <div id="uploadLabel" class="text-info">No file selected...</div>
                        <div class="progress progress-striped active" id="progressbar" aria-valuemin="0"
                             aria-valuemax="100">
                            <div class="bar" style="width: 0%;"></div>
                        </div>
                    </div>
                </div>
        </form>
    </div>
    <div class="span8">
        Here will be tetris glass
    </div>

</div>
</div>
<script>
    function showProgress(progress) {
        $('#progressbar .bar').css(
                'width',
                progress + '%'
        );
    }
    function startPollGameProgress(timestamp) {
        function poll() {
            $.ajax({ url:'${pageContext.request.contextPath}' + '/progress?timestamp=' + timestamp,
                success:function (data) {
                    if (data == null) {
                        $("#error").text("There is no progress available!");
                        return;
                    }
                    showProgress(data.progress);
                    if (data.progress >= 100) {
                        $("#uploadLabel").text("Game execution finished");
                        $('#infoPanel').removeClass('fade.in');
                        $('#infoPanel').addClass('fade');

                    } else {
                        poll();
                    }
                },
                error:function (xhr, ajaxOptions, thrownError) {
                    $("#error").text("Error on getting game progress. status:" + xhr.status + " error: " + thrownError);
                },
                dataType:"json", cache:false, timeout:300000 });
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

    /* Table initialisation */
    $(document).ready(function () {
        $('#gamelogs').dataTable({
            "sAjaxSource":'${pageContext.request.contextPath}/resources/testdata.json',
            "sDom":"<'row span12'<'span6'l><'span6'f>r>t<'row'<'span6'i><'span6'p>>",
            "sPaginationType":"bootstrap",
            "oLanguage":{
                "sLengthMenu":"_MENU_ records per page"
            }
        });
    });

    /*
     $(document).ready(function () {
     $('#gamelogs').dataTable({
     "bProcessing":true,
     "sAjaxSource":'${pageContext.request.contextPath}/resources/testdata.json',
     "sDom":"<'row'<'span6'l><'span6'f>r>t<'row'<'span6'i><'span6'p>>"
     });
     $.extend($.fn.dataTableExt.oStdClasses, {
     "sWrapper":"dataTables_wrapper form-inline"
     });
     }
     );
     */
</script>

</body>
</html>
