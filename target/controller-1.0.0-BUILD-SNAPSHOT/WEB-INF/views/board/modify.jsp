<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ include file="../includes/header.jsp" %>
<script>
	$(document).ready(function() {
		
	(function() {

		var bno = '<c:out value="${board.bno}"/>';
		
		$.getJSON("/board/getAttachList", {bno:bno}, function(arr) {
			var str = '';
			$(arr).each(function(i, attach) {
			
				
				if(attach.fileType){
					
					var fileCallPath = encodeURIComponent(attach.uploadPath + "/s_" + attach.uuid + "_" + attach.fileName);
					str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"'><div>";
					str += "<span>" + attach.fileName + "</span>";
					str += "<button data-file=\'"+fileCallPath+"\' data-type='image'  type='button' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
					str += "<img src='/display?fileName="+fileCallPath+"'>";
					str += "</div>";
					str += "</li>";	
				} else {
					
					str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"'><div>";
					str += "<span>" + attach.fileName + "</span><br/>";
					str += "<button data-file=\'"+fileCallPath+"\' data-type='file'  type='button' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
					str += "<img src='/resources/img/attach.png'>";
					str += "</div>";
					str += "</li>";	
				}
			});
			$(".uploadResult ul").html(str);
		})
	})();
		
	$(".uploadResult").on("click", "button", function(e) {
		if(confirm("Remove this file? ")){
			var targetLi = $(this).closest("li");
			targetLi.remove();
		}
	})
	
	var regex = new RegExp("(.*)\.(exe|sh|zip|alz)$");
	var maxSize = 5242880;
	
		function checkExtension(fileName, fileSize) {
			if(fileSize > maxSize){
				alert("파일 사이즈 큼");
				return false;
			}
			if(regex.test(fileName)){
				alert("파일 업로드 종류");
				return false;
			}
			return true;
			
		}
		
		var csrfHeaderName = "${_csrf.headerName}";
		var csrfTokenValue = "${_csrf.token}";
		
		
			$("input[type='file']").change(function(e) {
				var formData = new FormData;
				var inputFile = $("input[name='uploadFile']");
				var files = inputFile[0].files;
				
				for(var i = 0; i < files.length; i++){
					
					if( !checkExtension(files[i].name, files[i].size) ) {
						return false;
					}
					
					formData.append("uploadFile", files[i]);
				}
				$.ajax({
					url : "/uploadAjaxAction",
					processData : false,
					contentType : false,
					beforeSend : function(xhr) {
						xhr.setRequestHeader(csrfHeaderName,csrfTokenValue);
					},
					data : formData,
					type : 'POST',
					dataType : 'json',
					success : function(result) {
						showUploadResult(result);
					}		
				});//ajax
			});//function
			
			function showUploadResult(arr) {
				
				if(!arr || arr.length == 0) {
					return;
				}
				
				var uploadUL = $(".uploadResult ul");
				var str = '';
				$(arr).each(function(i, obj) {
					if (obj.image){
						var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
						str += "<li data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'><div>";
						str += "<sapn>" + obj.fileName + "</span>";
						str += "<button data-file=\'"+fileCallPath+"\' data-type='image' type='button' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
						str += "<img src='/display?fileName="+fileCallPath+"'>";
						str += "</div>";
						str += "</li>";
					} else {
						var fileCallPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" + obj.fileName);
						var fileLink = fileCallPath.replace(new RegExp(/\\/g), "/");
						str += "<li data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'><div>";
						str += "<sapn>" + obj.fileName + "</span>";
						str += "<button data-file=\'"+fileCallPath+"\' data-type='file' type='button' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
						str += "<img src='/resources/img/attach.png'></a>";
						str += "</div>";
						str += "</li>";
					}
				})
				uploadUL.append(str);
			}
			
		
	
	
		var formObj = $("form");
		
		$("button").on("click", function(e) {
			
			e.preventDefault();
			
			var oper = $(this).data("oper");
			
			if(oper === 'remove'){
				formObj.attr("action", "/board/remove");
			} else if (oper === 'list'){
				var pageNumTag = $("input[name='pageNum']").clone();
				var amountTag = $("input[name='amount']").clone();
				var typeTag = $("input[name='type']").clone();
				var keywordTag = $("input[name='keyword']").clone();
				formObj.attr("action", "/board/list").attr("method", "get");
				formObj.empty();
				formObj.append(pageNumTag);
				formObj.append(amountTag);
				formObj.append(typeTag);
				formObj.append(keywordTag);
			} else if(oper === 'modify'){
				var str = '';
				
				$(".uploadResult ul li").each(function(i, obj) {
					var jobj = $(obj);
					
					str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+jobj.data("filename")+"'>";
					str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+jobj.data("uuid")+"'>";
					str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
					str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+jobj.data("type")+"'>";
				})
				formObj.append(str).submit();
				
			}
			formObj.submit();
		});
		
		
	});//ready
</script>

 			<div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Board Modify Page</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                           Board Modify Page
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                        
                        <form role="form" action="/board/modify" method="post">
                                <input type="hidden" name="pageNum" value='<c:out value="${cri.pageNum}"/>'>
                        		<input type="hidden" name="amount" value='<c:out value="${cri.amount}"/>'>
                        		<input type="hidden" name="type" value='<c:out value="${cri.type}"/>'>
                        		<input type="hidden" name="keyword" value='<c:out value="${cri.keyword}"/>'>
                        	<div class="form-group">
                        		<label>Bno</label> <input class="form-control" name="bno"
                        		value='<c:out value="${board.bno}"/>' readonly="readonly">
                        	</div>
                        	<div class="form-group">
                        		<label>Title</label> <input class="form-control" name="title"
                        		value='<c:out value="${board.title}"/>'>
                        	</div>
                        	<div class="form-group">
                        		<label>Text area</label>
                        		<textarea rows="3" class="form-control" name="content"><c:out value="${board.content}"/>
                        		</textarea>
                        	</div>
                        	<div class="form-group">
                        		<label>Writer</label> <input class="form-control" name="writer"
                        		value='<c:out value="${board.writer}"/>' readonly="readonly">
                        	</div>
                        	<div class="form-group">
                        		<label>RegDate</label> <input  class="form-control" name="regdate"
                        		value='<fmt:formatDate pattern="yyyy/MM/dd" value="${board.regdate}"/>' readonly="readonly">
                        	</div>
                        	<div class="form-group">
                        		<label>UpdateDate</label> <input  class="form-control" name="updateDate"
                        		value='<fmt:formatDate pattern="yyyy/MM/dd" value="${board.updateDate}"/>' readonly="readonly">
                        	</div>
					<sec:authentication property="principal" var="pinfo"/>
						<sec:authorize access="isAuthenticated()">
						<c:if test="${pinfo.username eq board.writer}">
						
							<button type="submit" data-oper='modify' class="btn btn-default">Modify</button>
							<button type="submit" data-oper='remove' class="btn btn-default">Remove</button>
						
						</c:if>
						</sec:authorize>	
							<button type="submit" data-oper='list' class="btn btn-info">List</button>
                        </form>
                        
						</div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-6 -->
            </div>
            <!-- /.row -->
  <div class="bigPictureWrapper">
            	<div class="bicPicture">
            	</div>
            </div>
<style>

.uploadResult {
	width : 100%;
	background-color: gray;
}
.uploadResult ul {
	display: flex;
	flex-flow: row;
	justify-content: center;
	align-items: center;
}
.uploadResult ul li {
	list-style: none;
	padding: 10px;
	align-content: center;
	text-align: center;
}
.uploadResult ul li img {
	width : 100px;
}
.uploadResult ul li img span{
	color: white;
}
.bigPictureWrapper {
	position: absolute;
	display: none;
	justify-content: center;
	align-items: center;
	top: 0%;
	width: 100%;
	height: 100%;
	background-color: gray;
	z-index: 100;
	background:rgba(255,255,255,0.5);
}
.bigPicture {
	position: relative;
	display: flex;
	justify-content: center;
	align-items: center;
}
.bigPicture img {
	width: 600px;
}
</style>
            <div class="row">
 				<div col-lg-12>
 					<div class="panel panel-default">
 					
 						<div class="panel-heading">Files</div>
 						<div class="panel-body">
							<div class="form-gropu uploadDiv">
								<input type="file" name="uploadFile" multiple="multiple">
							</div>
 						
 							<div class="uploadResult">
 								<ul>
 								
 								</ul>
 							</div>
 						
 						
 						
 						
 						</div>	
 					</div>
 				</div>
 			</div>

<%@ include file="../includes/footer.jsp" %>
