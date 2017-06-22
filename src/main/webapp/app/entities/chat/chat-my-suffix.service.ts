import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { ChatMySuffix } from './chat-my-suffix.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ChatMySuffixService {

    private resourceUrl = 'api/chats';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(chat: ChatMySuffix): Observable<ChatMySuffix> {
        const copy = this.convert(chat);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(chat: ChatMySuffix): Observable<ChatMySuffix> {
        const copy = this.convert(chat);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<ChatMySuffix> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    queryByUser(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        let resourceUrl = "api/chats-user"
        return this.http.get(resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.creationtime = this.dateUtils
            .convertDateTimeFromServer(entity.creationtime);
    }

    private convert(chat: ChatMySuffix): ChatMySuffix {
        const copy: ChatMySuffix = Object.assign({}, chat);

        copy.creationtime = this.dateUtils.toDate(chat.creationtime);
        return copy;
    }
}
