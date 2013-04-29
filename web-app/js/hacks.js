var data = {items: [
    {value: "21", name: "Mick Jagger"},
    {value: "43", name: "Johnny Storm"},
    {value: "46", name: "Richard Hatch"},
    {value: "54", name: "Kelly Slater"},
    {value: "55", name: "Rudy Hamilton"},
    {value: "79", name: "Michael Jordan"}
]};

jQuery("#to").autoSuggest(data.items, {
    selectedItemProp: "name",
    searchObjProps: "name",
    minChars: 3,
    startText:"" }
);

jQuery("input[name='to']").watermark("Enter recipients here...")

jQuery(document).on("ready", "input[data-watermark]", function() {
    var comp = $(this);
    comp.watermark(comp.attr("data-watermark"))
});