var vm = new Vue({
	el:'#Slogin-box',
	data :{
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
		    ]
	  },
	methods: {
		pagejump:function(){
			var self = this;
			var tags = $('#tags').val();
			window.location.href='http://localhost:8080/framework-admin/modules/test/searchxq.html?id=' + tags;
		},
		autocomplete:function(){
			var self = this;
			$( "#tags" ).autocomplete({
			      source: self.availableTags
			});
		}
	},
	mounted:function(){
		this.autocomplete();
    },
	created: function(){
	}
});