/* Youtube Script*/
// 2. This code loads the IFrame Player API code asynchronously.
var tag = document.createElement('script');

tag.src = "https://www.youtube.com/iframe_api";
var firstScriptTag = document.getElementsByTagName('script')[0];
firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
 
// 3. This function creates an <iframe> (and YouTube player)
//    after the API code downloads.
var player; 
var viedoId = document.getElementsById("ppp").value;		// 
//DB 에서 url 을 받아 와야 함 
function onYouTubeIframeAPIReady() {
	
	alert(viedoId+"test");
	player = new YT.Player('content__player', {
		videoId: viedoId,                             <!-- videoId : url 의 동영상 재생 아이디-->
        events: {
            'onReady': onPlayerReady,
            'onStateChange': onPlayerStateChange
        }
    });
}

// 4. The API will call this function when the video player is ready.
function onPlayerReady(event) {
    event.target.playVideo();
}

// 5. The API calls this function when the player's state changes.
//    The function indicates that when playing a video (state=1),
//    the player should play for six seconds and then stop.
var done = false;
function onPlayerStateChange(event) {
    if (event.data == YT.PlayerState.PLAYING && !done) {
        //setTimeout(stopVideo, 6000);
        done = true;
    }
}
function stopVideo() {
    player.stopVideo();
}/**
 * Created by Soldesk on 2017-01-05.
 */
