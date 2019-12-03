$(function () {
	$.jgrid.defaults.styleUI = 'Bootstrap';
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/user/list',
        datatype: "json",
        colModel: [			
			{ label: '用户ID', name: 'userId', index: "user_id", width: 45, key: true },
			{ label: '用户名', name: 'username', width: 75 },
            { label: '所属域', name: 'domainName', sortable: false, width: 75 },
            { label: '所属部门', name: 'deptName', sortable: false, width: 75 },
			{ label: '邮箱', name: 'email', width: 90 },
			{ label: '手机号', name: 'mobile', width: 100 },
			{ label: '状态', name: 'status', width: 60, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}},
			{ label: '创建时间', name: 'createTime', index: "create_time", width: 85}
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        shrinkToFit: true,
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
var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
            
        }
    }
};
var domainSetting = {
	    data: {
	        simpleData: {
	            enable: true,
	            idKey: "domainId",
	            pIdKey: "parentId",
	            rootPId: -1
	        },
	        key: {
	            url:"nourl"
	        }
	    }
	};
var ztree;
var domainZtree;

var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            username: null
        },
        showList: true,
        areaShow:false,
        locationShow:false,
        title:null,
        roleList:{},
        locationOptions:[],
        user:{
        	areacode:'440604',
        	locationcode:'',
            status:1,
            deptId:null,
            deptName:null,
            domainId:null,
            domainName:null,
            permissionCode:null,
            roleIdList:[]
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },openWindowLayer:function(config){
        	var shadCloseCfg = true;
        	if(config.shadeClose===false){
        		shadCloseCfg = false;
        	}
        	layer.open({
        	  offset: config.offset?config.offset:'50px',
      		  type: config.type?config.type:1,
      		  area: config.area?config.area:['1000px','530px'],//[config.width?config.width:'1000px', config.height?config.height:'650px'],
      		  fixed: config.fixed?config.fixed:false, //不固定
      		  maxmin: config.maxmin?config.maxmin:true,
      		  //type: 1,
      		  title: config.title?config.title:'窗口',
      		  //closeBtn:(config.closeBtn==false)?false:config.closeBtn,
      		  //closeBtn: true,
      		  //area: '516px',
      		  skin: config.skin?config.skin:'layui-layer-white', //没有背景色
      		  shadeClose: config.shadeClose?config.shadeClose:false,
      		  content: config.content?config.content:$('#addPage'),
      		  btn: config.btn?config.btn:null,
                btn1: config.btn1?config.btn1:function (index) {
                    vm.saveOrUpdate(index);
                    //layer.close(index);
                }
      		});
        },openTreeWindowLayer:function(config){
        	vm.openWindowLayer(
        			{title:config.title?config.title:"选择",
        			content:config.content?config.content:null,
        			offset: '50px',
        			area: ['300px', '450px'],
        			btn: ['确定', '取消'],
        			btn1: config.btn1?config.btn1:null});
        },initToastr:function(config){
        	toastr.options = {
      			  "closeButton":true,
      			  "debug": false,
      			  "progressBar": false,
      			  "positionClass": config.positionClass?config.positionClass:"toast-top-center",
      			  "onclick": null,
      			  "showDuration": "1400",
      			  "hideDuration": "1000",
      			  "timeOut": "2000",
      			  "extendedTimeOut": "1500",
      			  "showEasing": "swing",
      			  "hideEasing": "linear",
      			  "showMethod": "fadeIn",
      			  "hideMethod": "fadeOut"
      			}
        },showMsg:function(state,stateMsg){
        	toastr[state?state:'success'](stateMsg?stateMsg:"操作成功");
        },optSuccess:function(config){
        	vm.showMsg();
        },optFail:function(config){
        	vm.showMsg("error","操作失败");
        },
        add: function(){
//        	layer.open({
//        		  type: 1,
//        		  area: ['1000px', '650px'],
//        		  fixed: false, //不固定
//        		  maxmin: true,
//        		  //type: 1,
//        		  title: '新增用户',
//        		  //closeBtn: true,
//        		  //area: '516px',
//        		  //skin: 'layui-layer-nobg', //没有背景色
//        		  shadeClose: true,
//        		  content: $('#addPage'),
//        		  btn: ['确定', '取消'],
//                  btn1: function (index) {
//                      vm.saveOrUpdate(windowIndex);
//                      //layer.close(index);
//                  }
//        		});
        	vm.title = "新增";
        	vm.openWindowLayer({title:vm.title,content:$('#addPage'),btn:['确定', '取消']});
            //vm.showList = false;
            vm.roleList = {};
            vm.user = {domainName:null,domainId:null,deptName:null, deptId:null, status:1, roleIdList:[]};

            //获取角色信息
            this.getRoleList();
            vm.getDomain();
            vm.getDept();
        },
        getDomain: function(){
            //加载域树
            $.get(baseURL + "sys/sysdomain/listAll", function(r){
                domainZtree = $.fn.zTree.init($("#domainTree"), domainSetting, r);
                var node = domainZtree.getNodeByParam("domainId", vm.user.domainId);
                if(node != null){
                	domainZtree.selectNode(node);

                    vm.user.domainName = node.name;
                }
            })
        },
        getDept: function(){
            //加载部门树
            $.get(baseURL + "sys/dept/list", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                var node = ztree.getNodeByParam("deptId", vm.user.deptId);
                if(node != null){
                    ztree.selectNode(node);

                    vm.user.deptName = node.name;
                }
            })
        },
        update: function () {
            var userId = getSelectedRow();
            if(userId == null){
                return ;
            }
//            vm.showList = false;
            vm.title = "修改";
            vm.openWindowLayer({title:vm.title,content:$('#addPage'),btn:['确定', '取消']});
            vm.getUser(userId);
            //获取角色信息
            this.getRoleList();
        },
        del: function () {
            var userIds = getSelectedRows();
            if(userIds == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/user/delete",
                    contentType: "application/json",
                    data: JSON.stringify(userIds),
                    success: function(r){
                        if(r.code == 0){
//                            alert('操作成功', function(){
//                                vm.reload();
//                            });
                        	vm.optSuccess();
                        	vm.reload();
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function (windowIndex) {
            var url = vm.user.userId == null ? "sys/user/save" : "sys/user/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.user),
                success: function(r){
                    if(r.code === 0){
                    	vm.optSuccess();
                    	vm.reload();
                        layer.close(windowIndex);
//                        alert('操作成功', function(){
//                            vm.reload();
//                            layer.close(windowIndex);
//                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        getUser: function(userId){
            $.get(baseURL + "sys/user/info/"+userId, function(r){
                vm.user = r.user;
                vm.user.password = null;
                vm.getDept();
                vm.getDomain();
                if(vm.user.permissionCode == 2){
            		vm.areaShow=true;
            	}
                if(vm.user.permissionCode == 1){
            		vm.areaShow=false;
            	}
                if(vm.user.permissionCode == 3){
            		vm.areaShow=true;
            		vm.locationShow=true;
            	}
                vm.getLocation();
            });
        },
        getRoleList: function(){
            $.get(baseURL + "sys/role/select", function(r){
                vm.roleList = r.list;
            });
        },
        
        deptTree: function(){
        	 vm.openTreeWindowLayer({title:"选择部门",content:jQuery("#deptLayer"),btn1: function (index) {
                 var node = ztree.getSelectedNodes();
                 //选择上级部门
                 vm.user.deptId = node[0].deptId;
                 vm.user.deptName = node[0].name;

                 layer.close(index);
             }});
        	
//            layer.open({
//                type: 1,
//                offset: '50px',
//                //skin: 'layui-layer-molv',
//                title: "选择部门",
//                area: ['300px', '450px'],
//                shade: 0,
//                shadeClose: false,
//                content: jQuery("#deptLayer"),
//                btn: ['确定', '取消'],
//                btn1: function (index) {
//                    var node = ztree.getSelectedNodes();
//                    //选择上级部门
//                    vm.user.deptId = node[0].deptId;
//                    vm.user.deptName = node[0].name;
//
//                    layer.close(index);
//                }
//            });
        },
        domainTree: function(){
        	vm.openTreeWindowLayer({title:"选择域",content:jQuery("#domainLayer"),btn1: function (index) {
                var node = domainZtree.getSelectedNodes();
                //选择上级部门
                vm.user.domainId = node[0].domainId;
                vm.user.domainName = node[0].name;

                layer.close(index);
            }
        });
//            layer.open({
//                type: 1,
//                offset: '50px',
////                skin: 'layui-layer-molv',
//                title: "选择域",
//                area: ['300px', '450px'],
//                shade: 0,
//                shadeClose: false,
//                content: jQuery("#domainLayer"),
//                btn: ['确定', '取消'],
//                btn1: function (index) {
//                    var node = domainZtree.getSelectedNodes();
//                    //选择上级部门
//                    vm.user.domainId = node[0].domainId;
//                    vm.user.domainName = node[0].name;
//
//                    layer.close(index);
//                }
//            });
        },
        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'username': vm.q.username},
                page:page
            }).trigger("reloadGrid");
        },
        changePermission: function(value){
        	var self = this;
        	if(value == 2){
        		self.areaShow=true;
        		self.locationShow=false;
        		self.user.locationcode='';
        	}
        	if(value == 1){
        		self.areaShow=false;
        		self.user.areacode='440601';
        		self.user.locationcode='';
        		self.locationShow=false;
        	}
        	if(value == 3){
        		self.areaShow=true;
        		self.locationShow=true;
        		
        	}
        },
        getLocation:function(){
        	var self = this;
        	if(self.user.permissionCode == 3){
        		$.get(baseURL + "sys/user/getLocationListByCode/"+self.user.areacode, function(r){
                   self.locationOptions=r.result;
                });
        	}
        }
    }
});
vm.initToastr({});



