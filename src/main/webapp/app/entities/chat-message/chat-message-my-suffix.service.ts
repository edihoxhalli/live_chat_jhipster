import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { ChatMessageMySuffix } from './chat-message-my-suffix.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ChatMessageMySuffixService {

    private resourceUrl = 'api/chat-messages';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(chatMessage: ChatMessageMySuffix): Observable<ChatMessageMySuffix> {
        const copy = this.convert(chatMessage);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(chatMessage: ChatMessageMySuffix): Observable<ChatMessageMySuffix> {
        const copy = this.convert(chatMessage);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<ChatMessageMySuffix> {
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
        entity.senttime = this.dateUtils
            .convertDateTimeFromServer(entity.senttime);
    }

    private convert(chatMessage: ChatMessageMySuffix): ChatMessageMySuffix {
        const copy: ChatMessageMySuffix = Object.assign({}, chatMessage);

        copy.senttime = this.dateUtils.toDate(chatMessage.senttime);
        return copy;
    }
}
