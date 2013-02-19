$(window).mousemove(function(event) {
				var ytop;
				var ybottom;
				
				if(event.pageY>60 && event.pageY<100){
					ytop=event.pageY;
				}
				
				  $("#line-top2").css({"top" : -ytop+60});
				  $("#line-top3").css({"top" : ytop-60});
				  
				});
