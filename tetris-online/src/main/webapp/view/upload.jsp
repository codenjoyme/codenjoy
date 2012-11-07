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
<script src="${pageContext.request.contextPath}/resources/js/jcanvas.min.js"></script>

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
    <div class="span2">
        <%@include file="glass_inc.jsp"%>
    </div>
    <div class="span6">
        <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered dataTable"
               id="scores">
            <thead>
            <tr role="row">
                <th role="columnheader" tabindex="0" aria-controls="gamelogs" rowspan="1"
                    colspan="1"
                    aria-sort="ascending">
                    Hero name
                </th>
                <th role="columnheader" tabindex="0" aria-controls="gamelogs" rowspan="1"
                    colspan="1">
                    Score
                </th>
                <th role="columnheader" tabindex="0" aria-controls="gamelogs" rowspan="1"
                    colspan="1">
                    Replay
                </th>
            </tr>
            </thead>

            <tbody role="alert" aria-live="polite" aria-relevant="all">
            <tr class="gradeA odd">
                <td class=" ">Hero1</td>
                <td class=" ">1</td>
                <td class=" ">11</td>
            </tr>
            <tr class="gradeA even">
                <td class=" ">Hero2</td>
                <td class=" ">2</td>
                <td class=" ">22</td>
            </tr>
            </tbody>
        </table>
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
                        refreshGameLogsTableData();
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


    function initOverviewDataTable()
    {
        oOverviewTable =$('#gamelogs').dataTable(
                {
                    "bPaginate": true,
                    "bJQueryUI": true,  // ThemeRoller-stöd
                    "bLengthChange": true,
                    "bFilter": true,
                    "bSort": false,
                    "bInfo": true,
                    "bAutoWidth": true,
                    "bProcessing": true,
                    "iDisplayLength": 10,
                    "aoColumnDefs": [
                        { "bVisible": true,  "aTargets": [ 0 ] },
                        {
                            "fnRender": function ( oObj ) {
                                return createButton(oObj, oObj.aData[1], '${requestScope["logged.user"]}');
                            },
                            "aTargets": [ 1 ]
                        }

                    ]
                });
    }

    function initScoresDataTable()
    {
        oOverviewTable =$('#scores').dataTable(
                {
                    "bPaginate": true,
                    "bJQueryUI": true,  // ThemeRoller-stöd
                    "bLengthChange": true,
                    "bFilter": false,
                    "bSort": false,
                    "bInfo": false,
                    "bAutoWidth": true,
                    "bProcessing": true,
                    "iDisplayLength": 10,
                    "aoColumnDefs": [
                        { "bVisible": true,  "aTargets": [ 0 ] },
                        { "bVisible": true,  "aTargets": [ 1 ] },
                        {
                            "fnRender": function ( oObj ) {
                                return createButton(oObj, oObj.aData[2], oObj.aData[0]);
                            },
                            "aTargets": [ 2 ]
                        }

                    ]
                });
    }

    function createButton(oObj, timestamp, playerName) {
        return '<button type="button" class="btn btn-success" ' +
                'onclick="replay(this, '+"'"+timestamp+"','" +playerName+ "'" + ')">' +
                '<i class="icon-play icon-white"></i>  <span>Replay</span>' +
                '</button>';
    }
    function refreshTable(tableId, urlData)
    {
        $.getJSON(urlData, null, function( json )
        {
            table = $(tableId).dataTable();
            oSettings = table.fnSettings();

            table.fnClearTable(this);

            for (var i=0; i<json.aaData.length; i++)
            {
                table.oApi._fnAddData(oSettings, json.aaData[i]);
            }

            oSettings.aiDisplay = oSettings.aiDisplayMaster.slice();
            table.fnDraw();
        });
    }

    function refreshGameLogsTableData()
    {
        refreshTable('#gamelogs', '${pageContext.request.contextPath}/logs');
    }

    function refreshScoresLogsTableData()
    {
        refreshTable('#scores', '${pageContext.request.contextPath}/scores');
    }

    $(document).ready(function () {
        initOverviewDataTable();
        refreshGameLogsTableData();
        initScoresDataTable();
        refreshScoresLogsTableData();
    });
</script>

</body>
</html>
