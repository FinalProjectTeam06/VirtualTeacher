
function send_new_code()
{
    alert("New code sent")
    fetch("http://localhost:8080/api/users/activate/new-code", {
        method: 'POST',
        headers: {
            'accept': '*/*'
        }
    })
        .then(res => {
        })
        .then(data => {
            console.log(data)
        })
        .catch(err => console.log(err))
}