var windowH = window.innerHeight;
document.body.innerHTML += "<style>#cy{height: " + windowH * .8 + "px}</style>"
var cy = cytoscape({
    container: document.getElementById('cy'),
    elements: []
});
cy.on('click', 'node', function (evt) {
    console.log(evt.target);
});

var updateOpertaionsPanel = function(sourceOpId){
    $.getJSON("/rest/getavailableoperations?sId="+(sourceOpId || ""), function(data){
        console.log(data);
        for(var item in data){
            var name = data[item].name;
            var btn = document.createElement("BUTTON");
            btn.innerText = name;
            btn.classList += "btn btn-info";
            document.getElementById("tools_inner").appendChild(btn);
            btn.addEventListener('click', function () {
                cy.add({
                    group: "nodes",
                    data: {label: name},
                    position: {x: 200, y: 200}
                });
            });
            cy.style().selector('node')
                .style({
                    'label': 'data(label)'
                }).update()
        }
    });
}
updateOpertaionsPanel();