import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { ChatMessageMySuffix } from './chatmessage-my-suffix.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ChatMessageMySuffixService {

    private resourceUrl = 'api/chatmessages';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(chatmessage: ChatMessageMySuffix): Observable<ChatMessageMySuffix> {
        const copy = this.convert(chatmessage);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    send(chatmessage: ChatMessageMySuffix): Observable<ChatMessageMySuffix> {
        const copy = this.convert(chatmessage);
        let url = 'api/chatmessages-send';
        return this.http.post(url, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(chatmessage: ChatMessageMySuffix): Observable<ChatMessageMySuffix> {
        const copy = this.convert(chatmessage);
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

    queryByChat(id:number): Observable<ResponseWrapper> {
        const options = createRequestOption();
        let url = 'api/chatmessages-by-chat';
        return this.http.get(`${url}/${id}`, options)
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

    private convert(chatmessage: ChatMessageMySuffix): ChatMessageMySuffix {
        const copy: ChatMessageMySuffix = Object.assign({}, chatmessage);

        copy.senttime = this.dateUtils.toDate(chatmessage.senttime);
        return copy;
    }
}
