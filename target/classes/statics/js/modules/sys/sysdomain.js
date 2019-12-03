$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/sysdomain/list',
        datatype: "json",
        colModel: [			
			{ label: 'domainId', name: 'domainId', index: 'domain_id', width: 50, key: true },
			{ label: '域ID（相当于业务系统id）', name: 'domainName', index: 'domain_name', width: 80 }, 			
			{ label: '域的基础路径（basepath)  格式为：ip:port/webcontext', name: 'basePath', index: 'basepath', width: 80 }, 			
			{ label: '', name: 'createUser', index: 'create_user', width: 80 }, 			
			{ label: '', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '预留字段，暂时没用', name: 'domainType', index: 'domain_type', width: 80 }			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		sysDomain: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.sysDomain = {};
		},
		update: function (event) {
			var domainId = getSelectedRow();
			if(domainId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(domainId)
		},
		saveOrUpdate: function (event) {
			var url = vm.sysDomain.domainId == null ? "sys/sysdomain/save" : "sys/sysdomain/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.sysDomain),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var domainIds = getSelectedRows();
			if(domainIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "sys/sysdomain/delete",
                    contentType: "application/json",
				    data: JSON.stringify(domainIds),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(domainId){
			$.get(baseURL + "sys/sysdomain/info/"+domainId, function(r){
                vm.sysDomain = r.sysDomain;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});