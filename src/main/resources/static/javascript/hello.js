console.log("Hello");
const toggleSidebar=()=>
{
    if($(".sidebar").is(":visible"))
    {
        $(".sidebar").css("dispaly","none");
        $(".content").css("margin-left","0%");
    }
    else
    {
        $(".sidebar").css("display","block");
        $(".content").css("margin-left","20%");
    }
};

const search=()=>
{
    console.log("searching-----");
    let query=$("#search-input").val();
    if(query == "")
    {
        $(".search-result").hide();

    }
    else
    {
        let url=`http://localhost:8080/search/${query}`;
        fetch(url)
        .then((response)=>{
        return response.json();
    })
    .then((data)=>{
        console.log(data);
    })
        $(".search-result").show();
    }

};
