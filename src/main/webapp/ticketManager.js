function fetchTickets() {
    fetch("ticketservlet")
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then(data => {
            document.getElementById("ticketCount").innerText = data.available;
            document.getElementById("userTickets").innerText = data.userTickets;
            document.getElementById("totalPrice").innerText = data.totalPrice;
            document.getElementById("cancelBtn").disabled = data.userTickets === 0;
        })
        .catch(error => {
            console.error("Error fetching tickets:", error);
        });
}

document.getElementById("bookBtn").addEventListener("click", function () {
    let qty = document.getElementById("ticketQty").value;

    if (qty.trim() === "" || qty <= 0) {
        let resultElement = document.getElementById("result");
        resultElement.classList.remove("success-message", "error-message");
        resultElement.classList.add("error-message");
        resultElement.innerText = "Enter a Valid Quantity for Booking";
        return;
    }

    fetch("ticketservlet?action=bookTickets", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({ qty: qty })
    })
        .then(response => response.json())
        .then(data => {
            let resultElement = document.getElementById("result");
            resultElement.classList.remove("success-message", "error-message");
            resultElement.classList.add(data.status === 'success' ? "success-message" : "error-message");
            resultElement.innerText = data.message;

            fetchTickets();
        })
        .catch(error => console.error("Error booking tickets:", error));
});

document.getElementById("cancelBtn").addEventListener("click", function () {
    let qty = document.getElementById("ticketQty").value;

    if (qty.trim() === "" || qty <= 0) {
        let resultElement = document.getElementById("result");
        resultElement.classList.remove("success-message", "error-message");
        resultElement.classList.add("error-message");
        resultElement.innerText = "Enter a Valid Quantity for Cancellation";
        return;
    }

    fetch("ticketservlet?action=cancelTickets", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({ qty: qty })
    })
        .then(response => response.json())
        .then(data => {
            let resultElement = document.getElementById("result");
            resultElement.classList.remove("success-message", "error-message");
            resultElement.classList.add(data.status === 'success' ? "success-message" : "error-message");
            resultElement.innerText = data.message;

            fetchTickets();
        })
        .catch(error => console.error("Error canceling tickets:", error));
});


setInterval(fetchTickets, 5000);

fetchTickets();
