$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'message/config/list',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 45, key: true },
            { label: '项目/事项名称', name: 'sourcename', index: "sourcename", width: 75 },
            { label: '发送账户', name: 'sender', index: "sender", width: 75 },
            { label: '接收部门', name: 'receivedept', sortable: false, width: 75 },
            { label: '接收账户', name: 'receiver', sortable: false, width: 75 },
           /* { label: '抄送账户', name: 'ccaccountname', width: 100 },*/
            { label: '创建时间', name: 'createtime', index: "createtime", width: 80},
            { label: '是否发送短信', name: 'issendphonevalue', sortable: false, width: 75 }
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

/*//菜单树
var menu_ztree;
var menu_setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "menuId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    },
    check:{
        enable:true,
        nocheckInherit:true
    }
};*/
//项目阶段结构树
var projectStage_Tree;
var projectStage_setting = {
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
    },
    check:{
        enable:true,
        nocheckInherit:true
    },
    callback: {
    	onClick: zTreeOnExpand
    }
};
//部门结构树
var dept_ztree;
var dept_setting = {
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
    },
    check:{
        enable:true,
        nocheckInherit:true
    },
    callback: {
    	onClick: zTreeOnExpand
    }
};
function zTreeOnExpand(event,treeId, treeNode) {       
    addNodes(treeNode);       
} 
function addNodes(treeNode){
    var param = {'deptId':treeNode.deptId};
    var treeObj = $.fn.zTree.getZTreeObj("dept_ztree");
    var parentZNode = dept_ztree.getNodeByParam("parentId", treeNode.pIdKey, null);//获取指定父节点
    var childNodes = dept_ztree.transformToArray(treeNode);//获取子节点集合
    if(childNodes.length==1 && childNodes[0].lastchildren==1){
     	$.ajax({
            method:'post',
            url: baseURL + 'message/config/findLastChildren',
            data:param,
            success:function(res){
                var data = [];
                if(res.deptList){
                    data = res.deptList; 
                }else{
                    data=[];
                }                                           
                dept_ztree.addNodes(childNodes[0],data, false);    //添加节点                           
            }
        })
    }
}
//数据树addNodes
var data_ztree;
var data_setting = {
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
    },
    check:{
        enable:true,
        nocheckInherit:true/*,
        chkboxType:{ "Y" : "", "N" : "" }*/
    },
    callback: {
    	onClick: dataTreeOnExpand
    }
};
function dataTreeOnExpand(event,treeId, treeNode) {       
	dataTreeAddNodes(treeNode);       
} 
function dataTreeAddNodes(treeNode){
    var param = {'deptId':treeNode.deptId};
    var treeObj = $.fn.zTree.getZTreeObj("data_ztree");
    var parentZNode = data_ztree.getNodeByParam("parentId", treeNode.pIdKey, null);//获取指定父节点
    var childNodes = data_ztree.transformToArray(treeNode);//获取子节点集合
    if(childNodes.length==1 && childNodes[0].lastchildren==1){
     	$.ajax({
            method:'post',
            url: baseURL + 'message/config/findLastChildrenForProject',
            data:param,
            success:function(res){
                var data = [];
                if(res.deptList){
                    data = res.deptList; 
                }else{
                    data=[];
                }                                           
                data_ztree.addNodes(childNodes[0],data, false);    //添加节点                           
            }
        })
    }
}
/*//数据树
var domainZtree;
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
};*/
var vm = new Vue({
    el:'#rrapp',
    data:{
    	MOBILE:'',
    	roleId:'',
    	SET_SENDER:'',
    	SET_RECEIVER:'',
    	IS_SEND_PHONE:1,
    	param:{},
    	status:0,
    	projectStageData:[
    		{deptId:'1',parentId:'0',name:'立项用地规划阶段'},
    		{deptId:'2',parentId:'0',name:'工程建设许可阶段'},
    		{deptId:'3',parentId:'0',name:'施工许可阶段'},
    		{deptId:'5',parentId:'0',name:'竣工验收阶段'}
    		],
    	changeType:1,
    	config:{},
    	userlist:[],
    	SENDER:'',
    	RECEIVER:'',
    	CC_ACCOUNT:'',
        q:{
            roleName: null
        },
        showList: true,
        title:null,
        role:{
            deptId:null,
            deptName:null,
            domainId:null,
            domainName:null
        }
    },
    created: function() {
   	 this.getUserList();
   },
    methods: {
        query: function () {
            vm.reload();
        },
        openWindowLayer:function(config){
        	var shadCloseCfg = true;
        	if(config.shadeClose===false){
        		shadCloseCfg = false;
        	}
        	layer.open({
        	  offset: config.offset?config.offset:'50px',
      		  type: config.type?config.type:1,
      		  area: config.area?config.area:['780px','380px'],//[config.width?config.width:'1000px', config.height?config.height:'650px'],
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
                    vm.updateIsSendPhone();
                    //layer.close(index);
                }
      		});
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.role = {deptName:null, deptId:null,domainName:null,domainId:null};
            //vm.getMenuTree(null);

            vm.getDept();
            //vm.getDomain();
            vm.getProjectStageTree();
            vm.getDataTree();
        },
        changeState: function(type){
        	if(type==1){
        		$("#xm").hide();
        		$("#xmjd").hide();
            	$("#spsx").show();
        	}
        	if(type==2){
        		$("#xm").show();
            	$("#spsx").hide();
            	$("#xmjd").hide();
        	}
        	if(type==3){
        		$("#xmjd").show();
            	$("#spsx").hide();
            	$("#xm").hide();
        	}
        	
        },
        updateIsSendPhone: function (){
        	alert(1);
        	var data= "id=" + vm.roleId+"&receiver="+ vm.SET_RECEIVER+"&IS_SEND_PHONE="+vm.IS_SEND_PHONE;
            $.ajax({
                type: "POST",
                url: baseURL + "message/config/setReceiverById",
                dataType: "json",
                data: data,
                success: function(r){
                    if(r.code == 0){
                    	 alert('操作成功', function(){
                             vm.reload();
                         });
                    }else{
                        //alert(r.msg);
                    }
                }
            });
        },
        update: function () {
            var roleId = getSelectedRow();
            if(roleId == null){
                return ;
            }
            vm.roleId=roleId;
            var data= "roleId=" + roleId;
            $.ajax({
                type: "POST",
                url: baseURL + "message/config/getReceiverById",
                dataType: "json",
                data: data,
                success: function(r){
                    if(r.code == 0){
                    	vm.SET_SENDER=r.receiverInfo.SENDER,
                    	vm.SET_RECEIVER=r.receiverInfo.RECEIVER,
                    	vm.IS_SEND_PHONE=r.receiverInfo.ISSENDPHONE,
                    	vm.MOBILE=r.receiverInfo.MOBILE,
                    	vm.title = "修改";
                        vm.openWindowLayer({title:vm.title,content:$('#addPage'),btn:['确定', '取消']});
                    }else{
                        alert(r.msg);
                    }
                }
            });
            

            /*vm.showList = false;
            vm.title = "修改";
            vm.getDataTree();
            vm.getMenuTree(roleId);

            vm.getDept();
            vm.getDomain();*/
        },
        del: function () {
            var roleIds = getSelectedRows();
            if(roleIds == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "message/config/delete",
                    contentType: "application/json",
                    data: JSON.stringify(roleIds),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        /*getRole: function(roleId){
            $.get(baseURL + "sys/role/info/"+roleId, function(r){
                vm.role = r.role;

                //勾选角色所拥有的菜单
                var menuIds = vm.role.menuIdList;
                for(var i=0; i<menuIds.length; i++) {
                    var node = menu_ztree.getNodeByParam("menuId", menuIds[i]);
                    menu_ztree.checkNode(node, true, false);
                }

                //勾选角色所拥有的部门数据权限
                var deptIds = vm.role.deptIdList;
                for(var i=0; i<deptIds.length; i++) {
                    var node = data_ztree.getNodeByParam("deptId", deptIds[i]);
                    data_ztree.checkNode(node, true, false);
                }

                vm.getDept();
                vm.getDomain();
            });
        },*/
        saveOrUpdate: function () {
            //获取选择的菜单
            var nodes = dept_ztree.getCheckedNodes(true);
            var projectNodes = data_ztree.getCheckedNodes(true);
            var projectStageNodes = projectStage_Tree.getCheckedNodes(true);
            
            if(nodes.length>0||projectNodes.length>0||projectStageNodes.length>0){
            	var approveList = new Array();
            	for(var i=0; i<nodes.length; i++) {
                	if(nodes[i].lastchildren==3){
                		approveList.push(nodes[i].deptId);
                	}
                }
            	var projectStageList = new Array();
            	for(var i=0; i<projectStageNodes.length; i++) {
                	projectStageList.push(projectStageNodes[i].deptId);
                }
            	var projectList = new Array();
            	for(var i=0; i<projectNodes.length; i++) {
                	if(projectNodes[i].lastchildren==3){
                		projectList.push(projectNodes[i].deptId);
                	}
                }
                if(vm.SENDER!='' && vm.RECEIVER!=''){
                	vm.config.sender=vm.SENDER;
                    vm.config.receiver=vm.RECEIVER;
                    vm.config.ccAccount=vm.CC_ACCOUNT;
                    if(vm.changeType==1){
                    	vm.config.approveList=approveList;
                    }else if(vm.changeType==2){
                    	vm.config.projectList=projectList;
                    }else if(vm.changeType==3){
                    	vm.config.projectStageList=projectStageList;
                    }
                    
                    console.log(vm.config);

                    var url ="message/config/save";
                    $.ajax({
                        type: "POST",
                        url: baseURL + url,
                        contentType: "application/json",
                        data: JSON.stringify(vm.config),
                        success: function(r){
                            if(r.code === 0){
                            	alert('操作成功');
                                /*alert('操作成功', function(){
                                    vm.reload();
                                });*/
                            }else{
                                alert(r.msg);
                            }
                        }
                    });
                }else{
                	alert("发送账户或接收账户不能为空！");
                }
            }else{
            	alert("请选择配置事项或项目！");
            }
            
            
        },
        getUserList: function() {
            //加载用户列表
            $.get(baseURL + "sys/user/userList", function(r){
                vm.userlist=r.userList;
            });
        },
        /*getMenuTree: function(roleId) {
            //加载菜单树
            $.get(baseURL + "sys/menu/list", function(r){
                menu_ztree = $.fn.zTree.init($("#menuTree"), menu_setting, r);
                //展开所有节点
                menu_ztree.expandAll(true);

                if(roleId != null){
                    vm.getRole(roleId);
                }
            });
        },*/
        getDataTree: function(roleId) {
            //加载菜单树
            $.get(baseURL + "message/config/projectList", function(r){
                data_ztree = $.fn.zTree.init($("#dataTree"), data_setting, r);
                //展开所有节点
                //data_ztree.expandAll(true);
            });
        },
        getProjectStageTree: function(roleId) {
            //加载菜单树
        	projectStage_Tree = $.fn.zTree.init($("#projectStageTree"), data_setting, vm.projectStageData);
                //展开所有节点
                //data_ztree.expandAll(true);
        },
        getDept: function(){
            //加载部门树
            $.get(baseURL + "sys/dept/list", function(r){
                dept_ztree = $.fn.zTree.init($("#deptTree"), dept_setting, r);
                var node = dept_ztree.getNodeByParam("deptId", vm.role.deptId);
                if(node != null){
                    dept_ztree.selectNode(node);

                    vm.role.deptName = node.name;
                }
            })
        },
       /* getDomain: function(){
            //加载域树
            $.get(baseURL + "sys/sysdomain/listAll", function(r){
                domainZtree = $.fn.zTree.init($("#domainTree"), domainSetting, r);
                var node = domainZtree.getNodeByParam("domainId", vm.role.domainId);
                if(node != null){
                    domainZtree.selectNode(node);

                    vm.role.domainName = node.name;
                }
            })
        },*/
        deptTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = dept_ztree.getSelectedNodes();
                    //选择上级部门
                    vm.role.deptId = node[0].deptId;
                    vm.role.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },
        projectStageTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择阶段",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = projectStage_Tree.getSelectedNodes();
                    //选择上级部门
                    vm.role.deptId = node[0].deptId;
                    vm.role.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },
        /*domainTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择域",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#domainLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = domainZtree.getSelectedNodes();
                    //选择上级部门
                    vm.role.domainId = node[0].domainId;
                    vm.role.domainName = node[0].name;

                    layer.close(index);
                }
            });
        },*/
        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'roleName': vm.q.roleName},
                page:page
            }).trigger("reloadGrid");
        }
    }
});