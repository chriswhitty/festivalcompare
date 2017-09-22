
class FestivalClient {

    createFestival(festival) {

        return fetch('/api/new-event', {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(festival)
        }).then((result) => result.json());

    }


}

export default FestivalClient