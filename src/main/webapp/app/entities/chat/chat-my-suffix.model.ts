import { BaseEntity } from './../../shared';

export class ChatMySuffix implements BaseEntity {
    constructor(
        public id?: number,
        public creationtime?: any,
        public user1Id?: number,
        public user2Id?: number,
    ) {
    }
}
