function getmap(logt, lat, bizplcnm, refineroadnmaddr, bsnstatenm, locplcfaclttelno, sigunnm, siguncd, licensg_de ){

    var hospitalDto = {
        "logt" : logt,
        "lat" : lat,
        "bizplcnm":bizplcnm,
        "refineroadnmaddr":refineroadnmaddr,
        "bsnstatenm":bsnstatenm,
        "locplcfaclttelno":locplcfaclttelno,
        "sigunnm":sigunnm,
        "siguncd":siguncd,
        "licensg_de" : licensg_de
    };

    $.ajax({
        url : "/hospitalmapcontroller",
        method: 'post',
        contentType: "application/json" ,  //  ajax 타입,
        data : JSON.stringify(hospitalDto),
        success: function(data) {
            if(data == "1"){
                location.href = "/hospitalmap"
            }
        }
    })
}

function getmap2(){
    lat = $("#lat").val();
    logt = $("#logt").val();
}
$(document).ready(function (callback){
    hospitalstatus();
});
/*function getmap(){

    $(function() {
        alert('hello')
        var mapContainer = document.getElementById('map'), // 지도를 표시할 div

            mapOption = {
                center: new kakao.maps.LatLng(lat, logt), // 지도의 중심좌표
                level: 3, // 지도의 확대 레벨
                mapTypeId : kakao.maps.MapTypeId.ROADMAP // 지도종류
            };
        // 지도를 생성한다
        var map = new kakao.maps.Map(mapContainer, mapOption);
    });
}*/


/*var mapContainer = document.getElementById('map'), // 지도를 표시할 div

mapOption = {
    center: new kakao.maps.LatLng(37.56660, 126.97949), // 지도의 중심좌표
    level: 3, // 지도의 확대 레벨
    mapTypeId : kakao.maps.MapTypeId.ROADMAP // 지도종류
};

// 지도를 생성한다
var map = new kakao.maps.Map(mapContainer, mapOption);*/
function keyevent(){
    stat = $("#h_status").val();
    stat2 = $("#h_keyword").val();
    stat3 = $("#h_search").val();

    $.ajax({
        url : "/hospitaltable"+"?status="+stat+"&keyword="+stat2+"&search="+stat3,
        success: function(data) {
            $("#research").html("");
            $("#research").html(data);
        }
    })
}

function hospitalstatus(){
    stat = $("#h_status").val();
    stat2 = $("#h_keyword").val();
    stat3 = $("#h_search").val();
    $.ajax({
        url : "/hospitaltable"+"?status="+stat+"&keyword="+stat2+"&search="+stat3,
        success: function(data) {
            $("#research").html("");
            $("#research").html(data);
        }
    })
}

function hospitalpage(pages){
    $.ajax({
        url : "/hospitalpaging"+"?page="+pages,
        success: function(data) {
            $("#research").html("");
            $("#research").html(data);
        }
    })

}