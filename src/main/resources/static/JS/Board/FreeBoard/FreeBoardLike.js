function likeClick(bno, cano, mno){
    $.ajax({
        url: "/Board/Free/ClickFreeBoardLike",
        data:{"bno":bno,"cano":cano,"mno":mno},
        success: function(result) {
            if(result == 1){
                $("#LikeBox").load(location.href + ' #LikeBox');
            }
            else if(result == 2){
                $("#LikeBox").load(location.href + ' #LikeBox');
            }
            else {alert("버그 발생!!");}
        }
    });
}