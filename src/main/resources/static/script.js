document.getElementById('apiForm').addEventListener('submit',function(event)){
    event.preventDefault();

    const formData = new FormData(this);
    const client = {
                firstName: formData.get('firstName'),
                lastName: formData.get('lastName'),
                age: formData.get('age'),
                telephone: formData.get('telephone'),
                email: formData.get('email'),
                password: formData.get('password')
    };

    fetch('http://localhost:8080/client'.{
        method: 'Post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(client)

    })
    .then(response => response.json())
    .then(data => {
         console.log('Success:', data);
    })
    .catch((error) => {
            console.error('Error:', error);
    });
});