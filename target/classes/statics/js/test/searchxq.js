var main = new Vue({
    el:".mainbox",
    data:{
    	title:'111',
		availableTags:[
		      "ActionScript",
		      "ACpleScript",
		      "Asp",
		      "BASIC",
		      "C",
		      "C++",
		      "Clojure",
		      "COBOL",
		      "ColdFusion",
		      "Erlang",
		      "Fortran",
		      "Groovy",
		      "Haskell",
		      "Java",
		      "JavaScript",
		      "Lisp",
		      "Perl",
		      "PHP",
		      "Python",
		      "Ruby",
		      "Scala",
		      "Scheme"
		    ],
		    tags:'',
		    aa:'',
		    Scontent:[{title:"html中如何将li标签的点",content:"Tweak a Classic World Pack #11.11#福利 全部originals定制产品 全部跑步定制产品全部篮球定制产品全部个性印制产品 COLLECTIONS 登录 400-999-5999 在线客.",url:'http://www.baidu.com'}
		    ,{title:"html2222中如何将li标签的点",content:"2222Tweak a Classic World Pack #11.11#福利 全部originals定制产品 全部跑步定制产品全部篮球定制产品全部个性印制产品 COLLECTIONS 登录 400-999-5999 在线客.",url:'http://www.baidu.com'}
		    ,{title:"html3333中如何将li标签的点",content:"3333Tweak a Classic World Pack #11.11#福利 全部originals定制产品 全部跑步定制产品全部篮球定制产品全部个性印制产品 COLLECTIONS 登录 400-999-5999 在线客.",url:'http://www.baidu.com'}
		    ,{title:"html4444中如何将li标签的点",content:"4444Tweak a Classic World Pack #11.11#福利 全部originals定制产品 全部跑步定制产品全部篮球定制产品全部个性印制产品 COLLECTIONS 登录 400-999-5999 在线客.",url:'http://www.baidu.com'}
		    ,{title:"html4444中如何将li标签的点",content:"4444Tweak a Classic World Pack #11.11#福利 全部originals定制产品 全部跑步定制产品全部篮球定制产品全部个性印制产品 COLLECTIONS 登录 400-999-5999 在线客.",url:'http://www.baidu.com'}
		    ,{title:"html4444中如何将li标签的点",content:"4444Tweak a Classic World Pack #11.11#福利 全部originals定制产品 全部跑步定制产品全部篮球定制产品全部个性印制产品 COLLECTIONS 登录 400-999-5999 在线客.",url:'http://www.baidu.com'}
		    ,{title:"html4444中如何将li标签的点",content:"4444Tweak a Classic World Pack #11.11#福利 全部originals定制产品 全部跑步定制产品全部篮球定制产品全部个性印制产品 COLLECTIONS 登录 400-999-5999 在线客.",url:'http://www.baidu.com'}
		    ,{title:"html4444中如何将li标签的点",content:"4444Tweak a Classic World Pack #11.11#福利 全部originals定制产品 全部跑步定制产品全部篮球定制产品全部个性印制产品 COLLECTIONS 登录 400-999-5999 在线客.",url:'http://www.baidu.com'}
		    ,{title:"html4444中如何将li标签的点",content:"4444Tweak a Classic World Pack #11.11#福利 全部originals定制产品 全部跑步定制产品全部篮球定制产品全部个性印制产品 COLLECTIONS 登录 400-999-5999 在线客.",url:'http://www.baidu.com'}
		    ,{title:"html4444中如何将li标签的点",content:"4444Tweak a Classic World Pack #11.11#福利 全部originals定制产品 全部跑步定制产品全部篮球定制产品全部个性印制产品 COLLECTIONS 登录 400-999-5999 在线客.",url:'http://www.baidu.com'}
		    ]
    },
    created:function(){
		this.cutout();
    },
    mounted:function(){
    	this.autocomplete();
    	this.page();
    	document.addEventListener('scroll', this.onScroll);
    },
    methods:{
    	autocomplete: function(){
			var self = this;
			$( "#tags" ).autocomplete({
			      source: self.availableTags
			});
		},
		pagejump:function(){
			var self = this;
			console.log("1111")
			var tags = $('#tags').val();
			self.tags = tags;
			console.log(tags)
			
		},
		cutout:function(){
			   var self = this; 
			   var url = location.search; //获取url中"?"符后的字串   
			   var theRequest = new Object();   
			   if (url.indexOf("?") != -1) {   
			      var str = url.substr(1);   
			      strs = str.split("&");   
			      for(var i = 0; i < strs.length; i ++) {   
			         theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);   
			      }   
			   }
			   self.tags = theRequest.id;
			   return theRequest;   
		},
		page:function(){
			      new Page({
			          id: 'pagination',
			          pageTotal: 50, //必填,总页数
			          pageAmount: 10,  //每页多少条
			          dataTotal: 500, //总共多少条数据
			          curPage:1, //初始页码,不填默认为1
			          pageSize: 5, //分页个数,不填默认为5
/*			           showPageTotalFlag:true,  //是否显示数据统计,不填默认不显示
*/			          showSkipInputFlag:true, //是否支持跳转,不填默认不显示
			          getPage: function (page) {
			              //获取当前页数
			             console.log(page);
			          }
			      })
		},
		onScroll () {
			var sticky = document.querySelector('.bigbox1');
	        var origOffsetY = sticky.offsetTop;
	        console.log(origOffsetY,window.scrollY)
	        window.scrollY > origOffsetY ? sticky.classList.add('fixed') : sticky.classList.remove('fixed');
	        
          },
    }
})