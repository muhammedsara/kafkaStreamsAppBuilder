var windowH = window.innerHeight;
document.body.innerHTML += "<style>#cy{height: " + windowH * .8 + "px}</style>"
var cy = cytoscape({
    container: document.getElementById('cy'),
    elements: []
});
cy.on('click', 'node', function (evt) {
    console.log(evt.target);
});
var btnAddSrc = document.getElementById('addSource');
btnAddSrc.addEventListener('click', function () {
    cy.add({
        group: "nodes",
        data: {weight: 75},
        position: {x: 200, y: 200}
    });
});

var updateOpertaionsPanel = function(sourceOpId){
    $.getJSON("/rest/getavailableoperations?sId="+(sourceOpId || ""), function(data){
        console.log(data);
    });
}
updateOpertaionsPanel();