## Ресурсы

### Пользователи (`/users`)

- **GET /users**: получить список всех пользователей.
- **GET /users/{id}**: получить информацию о конкретном пользователе.

### Объявления (`/ads`)

- **GET /ads**: получить список всех объявлений.
- **GET /ads/{id}**: получить информацию о конкретном объявлении.
- **GET /ads/current**: получить список активных объявлений текущего пользователя.
- **GET /ads/closed**: получить список выкупленых объявлений текущего пользователя.
- **GET /ads/purchased**: получить список объявлений, которые были выкуплены текущим пользователем.
- **POST /ads**: создать новое объявление.
- **PUT /ads/{id}**: обновить существующее объявление.
- **PUT /ads/{id}/premium**: сделать объявление премиальным (продвинуть в выдаче).
- **DELETE /ads/{id}**: удалить объявление.

### Комментарии (`/ads/{adId}/comments`)

- **GET /ads/{adId}/comments**: получить список всех комментариев данного объявления.
- **POST /ads/{adId}/comments**: опубликовать новый комментарий под объявлением.
- **PUT /ads/{adId}/comments/{commentId}**: обновить комментарий под объявлением.
- **DELETE /ads/{adId}/comments/{commentId}**: удалить комментарий под объявлением.

### Предложения (`/proposals`)
- **GET /proposals/sent**: получить список всех предложений от текущего пользователя.
- **GET /proposals/received**: получить список всех предложений текущему пользователю.
- **POST /proposals**: отправить новое предложение.
- **DELETE /proposals/received/{id}**: отклонить присланное предложение.

### Переписки
- **GET /conversations**: получить список всех активных переписок.
- **GET /conversations/{id}**: получить конкретную переписку.
- **POST /ads/{adId}/discuss**: начать переписку по поводу объявления.
- **DELETE /conversations/{id}**: удалить переписку вручную.

### Сообщения  (`/conversations/{id}/messages`)
- **POST /conversations/{id}/messages**: отправить сообщение в переписку.
