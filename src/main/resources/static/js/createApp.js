var operationId;
var nodeCount = 0;
var lastClikedCy;
var btnSave;
var graphData = JSON.parse(document.getElementById("appData").innerText || "{}") ;

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

cy.on('click', 'node', function (evt) {
    lastClikedCy = evt.target;
    $('#propBody').remove();
    operationId = this.data().operatorId;
    var returnTypeId = this.data().returnTypeId;
    $.getJSON("/rest/getavailableproperties?opId=" + (operationId || ""), function (data) {
        //console.log(data);
        var ht = "<tbody id='propBody'></tbody>";
        $('#propTable').append(ht);
        for (var item in data) {
            var name = data[item].name;
            var html = "<tr><td>" + name + "</td><td><input type='text'></td></tr>";
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
        //console.log(data);

        for (var item in data) {
            var name = data[item].name;
            var btn = document.createElement("BUTTON");
            btn.innerText = name;
            btn.classList += "btn btn-info";
            document.getElementById("tools_inner").innerHTML = "";
            document.getElementById("tools_inner").appendChild(btn);
            btn.addEventListener('click', function () {
                console.log("source: " + operationId + " target " + data[item].id);
                var cyData = [{
                    group: "nodes",
                    data: {
                        id: nodeCount,
                        label: name,
                        operatorId: data[item].id,
                        returnTypeId: data[item].returnType.id

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
            });
            var btn = document.createElement("BUTTON");
            btn.innerText = name;
            btn.classList += "btn btn-info";
            document.getElementById("tools_inner").innerHTML = "";
            document.getElementById("tools_inner").appendChild(btn);
            btn.addEventListener('click', function () {
                console.log("source: " + operationId + " target " + data[item].id);
                var cyData = [{
                    group: "nodes",
                    data: {
                        id: nodeCount,
                        label: name,
                        operatorId: data[item].id,
                        returnTypeId: data[item].returnType.id

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
                .update()
        }
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


updateOpertaionsPanel();
updateCyGraph();

