$(document).ready(function() {
  $('#freebcontents').summernote({
    height: 300,                 // 에디터 높이
    minHeight: null,             // 최소 높이
    maxHeight: null,             // 최대 높이
    focus: true,                  // 에디터 로딩후 포커스를 맞출지 여부
    lang: "ko-KR",               // 한글 설정
    placeholder:"내용입력"
  });
});

//게시판 지우기
function freeboardDelete(bno){
    alert("삭제버튼 눌렀을때  bno 나옴? : "+ bno);
    $.ajax({
        url: "/Board/Free/FreeBoardDelete",
        data:{"bno":bno},
        success: function(result){
            alert(result);
            if(result == 1){
                location.href = "/freeboard";
            }else{
                alert("오류발생");
            }
        }
    });
}

//드래그앤 드롭으로 자유게시판 글 수정하기
var fileList = []; //파일 정보를 담아 둘 배열
var fileIndex = 0; //파일 번호
$(document).ready(function(){
    //드래그앤드랍
    $("#fileDrop").on("dragenter", function(e){
        e.preventDefault();
        e.stopPropagation();
    }).on("dragover", function(e){
        e.preventDefault();
        e.stopPropagation();
        $(this).css({
            background: "rgba(255,206,147,1)",
            transition: "all 0.3s ease"
        });
    }).on("dragleave", function(e){
        e.preventDefault();
        e.stopPropagation();
        $(this).css("background-color", "#FFF");
    }).on("drop", function(e){
        e.preventDefault();

        var files = e.originalEvent.dataTransfer.files;
        if(files != null && files != undefined){
            var tag = "";
            for(i=0; i<files.length; i++){
                var f = files[i];
                fileList.push(f);
                var fileName = f.name;
                var fileSize = f.size / 1024 / 1024;
                fileSize = fileSize < 1 ? fileSize.toFixed(3) : fileSize.toFixed(1);
                tag +=
                        "<div id='miniFileList"+fileIndex +"' class='fileList'>" +
                            "<span class='fileName'>"+fileName+"</span>" +
                            "<span class='fileSize'>"+fileSize+" MB</span>" +
                            "<span class='clear'><a href='#' onclick='deleteFile(" + fileIndex + ")'; return false; style='margin-left:5px;'>삭제</a></span>" +
                        "</div>";
                fileIndex++;
            }
            $(this).append(tag);
        }

        $(this).css("background-color", "#FFF");
    });

    //저장
    $(document).on("click", "#save2", function(){

        var bno = $("#bno").val();
        //alert(bno);
        var formData = new FormData($("#fileForm")[0]);
        if(fileList.length > 0){
            fileList.forEach(function(f){
                formData.append("fileList", f);
            });
        }

        $.ajax({
            url : "/Board/Free/FreeBoardUpdate",
            data : formData,
            type:'POST',
            enctype:'multipart/form-data',
            processData:false,
            contentType:false,
            dataType:'json',
            cache:false,
            success:function(result){
            //0이면 오류발생 //1이면 글쓰기 완료 //2이면 내용이나 제목이 없음 -> 글쓰기 실패 //3이면??
                if(result == 1){
                    alert("글 수정 완료.");
                    location.href = "/Board/Free/FreeBoardView/"+bno;
                }else if(result == 2){
                    alert("로그인을 먼저 해주세요.");
                    location.href = "/Member/Login";
                }else if(result ==3){
                    alert("내용을 입력해주세요.");
                }else {
                    alert("버그 발생!");
                }
            }
        });
    });
});

//수정페이지 이미지 개별삭제
function boardImgDelete(bno, bimg){

    //alert(bno +", " + bimg);
    $.ajax({
        url:"/Board/Free/FreeBoardImgDelete",
        data:{"bno":bno,"bimg":bimg},
        success: function(result) {
            //alert(result);
            location.reload();
        }
    });
}

//수정페이지 첨부파일 개별 삭제 클릭시
 function deleteFile(fIndex){
    // 파일 사이즈 배열 삭제
    delete fileList[fIndex];
    // 업로드 파일 테이블 목록에서 삭제
    $("#miniFileList" + fIndex).remove();
}
