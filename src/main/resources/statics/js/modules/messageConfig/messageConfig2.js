$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'message/config/list',
        datatype: "json",
        colModel: [
            { label: 'ID', name: 'id', index: "id", width: 45, key: true },
            { label: '项目/事项名称', name: 'sourcename', index: "sourcename", width: 75 },
            { label: '发送账户', name: 'sendername', index: "sendername", width: 75 },
            { label: '接收账户', name: 'receivername', sortable: false, width: 75 },
            { label: '抄送账户', name: 'ccaccountname', width: 100 },
            { label: '创建时间', name: 'createtime', index: "createtime", width: 80}
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
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.role = {deptName:null, deptId:null,domainName:null,domainId:null};
            //vm.getMenuTree(null);

            vm.getDept();
            //vm.getDomain();
            
            vm.getDataTree();
        },
        /*update: function () {
            var roleId = getSelectedRow();
            if(roleId == null){
                return ;
            }

            vm.showList = false;
            vm.title = "修改";
            vm.getDataTree();
            vm.getMenuTree(roleId);

            vm.getDept();
            vm.getDomain();
        },*/
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
            
            if(nodes.length>0||projectNodes.length>0){
            	var approveList = new Array();
            	for(var i=0; i<nodes.length; i++) {
                	if(nodes[i].lastchildren==3){
                		approveList.push(nodes[i].deptId);
                	}
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
                    vm.config.approveList=approveList;
                    vm.config.projectList=projectList;
                    console.log(vm.config);

                    var url ="message/config/save";
                    $.ajax({
                        type: "POST",
                        url: baseURL + url,
                        contentType: "application/json",
                        data: JSON.stringify(vm.config),
                        success: function(r){
                            if(r.code === 0){
                                alert('操作成功', function(){
                                    vm.reload();
                                });
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