var operationId;
var nodeCount = 0;
var lastClikedCy;
var btnSave;
var graphData = JSON.parse(document.getElementById("appData").innerText || "{}") ;
var codeValidationIntervalMillis = 2000;

var updateCyGraph = function(){
    cy.json(graphData);
}

var isNodeLeaf = function (node) {
    return cy.nodes().leaves().filter(x => x.id() == node.id()).length;
}


var updateExistingApp = function(){
    graphData = JSON.stringify(cy.json());
    var appId = document.getElementById("appId").value;
    $.post("/rest/updateapp", {
        appId : appId,
        appJson : graphData
    }).done(function (data) {
        alert("saved");
    });
}

var saveNewApp = function(){
    //save the graphData to into variable
    graphData = JSON.stringify(cy.json());
    var newAppName = prompt("Please enter your application");
    if (newAppName != null) {
        $.post("/rest/savenewapp", {
            appName : newAppName,
            appJson : graphData
        }).done(function (data) {
            alert("saved");
        });
    }
}

var isNewApp = function(){
    return document.getElementById("appId").value == "";
}

var windowH = window.innerHeight;
document.body.innerHTML += "<style>#cy{height: " + windowH * .8 + "px}</style>"
var cy = cytoscape({
    container: document.getElementById('cy'),
    elements: []
});
// any click on canvas makes toolbox as default
cy.on('click', function (event) {
    var evtTarget = event.target;
    if (evtTarget === cy) {
        lastClikedCy = null;
        updateOpertaionsPanel();
    }
});

var updatePropertyOfNode = function(inputElement){
    var propName = inputElement.getAttribute("data-prop-name");
    if(lastClikedCy.data().properties==null){
        lastClikedCy.data().properties={};
    }
    lastClikedCy.data().properties[propName] = inputElement.value;
}

cy.on('click', 'node', function (evt) {
    lastClikedCy = evt.target;
    $('#propBody').remove();
    operationId = this.data().operatorId;
    var properties = this.data().properties;
    var returnTypeId = this.data().returnTypeId;
    $.getJSON("/rest/getavailableproperties?opId=" + (operationId || ""), function (data) {
        //console.log(data);
        var ht = "<tbody id='propBody'></tbody>";
        $('#propTable').append(ht);
        for (var item in data) {
            var name = data[item].name;
            var propertyValue = (properties || {})[name] || "";
            var html = "<tr><td>" + name +
                "</td><td><input type='text' data-prop-name='"+name+"' " +
                "oninput='updatePropertyOfNode(this)' value='"+propertyValue+"'></td></tr>";
            $('#propTable').append(html);
        }

    });

    updateOpertaionsPanel(returnTypeId);


});

btnSave = document.getElementById("btnSave");
btnSave.addEventListener("click", function () {
    if (isNewApp()){
        saveNewApp();
    } else {
        updateExistingApp();
    }
});



var updateOpertaionsPanel = function (sourceOpId) {
    $.getJSON("/rest/getavailableoperations?sId=" + (sourceOpId || ""), function (data) {
        document.getElementById("tools_inner").innerHTML = "";
        for (var item in data) {
            var name = data[item].name;
            var btn = document.createElement("BUTTON");
            btn.innerText = name;
            btn.classList += "btn btn-info";
            document.getElementById("tools_inner").appendChild(btn);
            if(cy.json().elements.nodes){
                nodeCount = cy.json().elements.nodes.length;
            }
            // this function helps preventing scope
            (function (data,item, name){
                btn.addEventListener('click', function () {
                    var cyData = [{
                        group: "nodes",
                        data: {
                            id: nodeCount,
                            label: name,
                            operatorId: data[item].id,
                            returnTypeId: data[item].returnType.id,
                            properties: {}
                        },
                        position: {x: 200, y: 200}
                    }];
                    if (lastClikedCy) {
                        cyData.push({
                            group: "edges",
                            data: {
                                source: lastClikedCy.id(),
                                target: nodeCount
                            }

                        });
                    }
                    cy.add(cyData);
                    nodeCount++;
                    $("#btnRemoveNode").remove();
                });
            })(data,item, name);
      } // end-for

      updateCyStyle(cy);
      if (sourceOpId && isNodeLeaf(lastClikedCy)) {
            var btnRemove = document.createElement("BUTTON");
            btnRemove.innerText = "Remove";
            btnRemove.classList += "btn btn-sm btn-danger";
            btnRemove.setAttribute("id", "btnRemoveNode");
            document.getElementById("tools_inner").appendChild(btnRemove);
            btnRemove.addEventListener('click', function () {

                cy.remove(lastClikedCy);
                lastClikedCy = null;
                updateOpertaionsPanel();
            });
        }
    });
}

function updateCyStyle(cy){
    cy.style()
        .selector('node')
        .style({
            'label': 'data(label)',
            shape: 'rectangle'
        })
        .selector("edge")
        .style({
            'curve-style': "bezier",
            'target-arrow-shape': 'triangle'
        })
        .update();
}

document.getElementById("btnGenerateCode").addEventListener('click', generateCode);

function generateCode(){
    var graphData = cy.json();
    var edges = (graphData["elements"]["edges"] || []).map(x=>x.data);
    var nodes = (graphData["elements"]["nodes"] || []).map(x=>x.data);

    $.post("/app/generateCode", {
        appJson : JSON.stringify({
            edges: edges,
            nodes: nodes
        })
    }).done(function (response) {
        // intentionally made global
        aceEditor = ace.edit("editor", {
            theme: "ace/theme/kuroir",
            mode: "ace/mode/java",
            autoScrollEditorIntoView: true,
            maxLines: 300,
            minLines: 20
        });
        aceEditor.setValue(response);
        $("#generatedCodeModal").modal('show');
    });
}

var prevCode = "";
var validateCodeTimer = setInterval(
    function(){
        var isModalVisible = $("#generatedCodeModal").is(":visible");
        if (isModalVisible) {
            var code = aceEditor.getValue();
            // if no changes, do not check
            if (prevCode === code) {
                return;
            }
            prevCode = code;
            $.post("/app/validateCode", {code:code}).done(
                function(resp){
                    clearErrorsFromEditor();
                    parseCompilerMessage(resp);
                    console.log(resp);
                }
            );
        }
    }, codeValidationIntervalMillis
);

function parseCompilerMessage(resp){
    var parts = resp.split(/\.java:*/);
    for(var i in parts){
        var e = parts[i];
        var text = e.split("^")[0];
        var line = text.split(":")[0];
        var col = 0;
        addErrorToEditor(text,line - 1,col);
    }
}

function clearErrorsFromEditor(){
    aceEditor.getSession().setAnnotations([]);
}

function addErrorToEditor(text, row, col){
    aceEditor.getSession().setAnnotations(aceEditor.getSession().$annotations.concat([{
        row: row,
        column: col,
        text: text,
        type: "error" // also warning and information
    }]));
}

updateOpertaionsPanel();
updateCyGraph();

