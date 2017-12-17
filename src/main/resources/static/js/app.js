var stompClient = null;
var dataRow =0;
var chatRow =0;
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#crawArea").html("");
    $("#chatRoom").html("");
}

function connect() {
    var socket = new SockJS('/webSocks');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/push/newData', function (newCrawData) {
            showData(JSON.parse(newCrawData.body));
        });
        stompClient.subscribe('/push/chat', function (chat) {
        	showDataChat(JSON.parse(chat.body));
        });
    });
    
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMsg() {
    
	stompClient.send("/app/chat", {}, JSON.stringify({'message': $("#message").val()}));
	
}

function showData(newCrawData) {
	dataRow++;
	if(dataRow > 19){
		$("#crawArea td:last").remove();
	}
    $("#crawArea").prepend(
    		"<tr><td>" + newCrawData.no + "</td>" +
    		"<td>" + newCrawData.siteName + "</td>" +
    		"<td>" + newCrawData.createdTime + "</td>" +
    		"<td>" + newCrawData.subject + "</td>" + 
    		"<td>" + newCrawData.boardWriteDate + "</td>" +
    		"<td>" +"<a href='" +newCrawData.link + "'>[link]</a></td></tr>" 
    		);
}

function showDataChat(chatObj) {
	chatRow++;
	if(chatRow > 9){
		$("#chatRoom td:last").remove();
	}
	$("#chatRoom").prepend("<tr><td>" + chatObj.message+ "</td></tr>");
}


$(function () {
	connect();
    $("form").on('submit', function (e) {
    	$("#message").val("");
    	$("#message").click();
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMsg(); });
});