import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ChatTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ChatMessageMySuffixDetailComponent } from '../../../../../../main/webapp/app/entities/chatmessage/chatmessage-my-suffix-detail.component';
import { ChatMessageMySuffixService } from '../../../../../../main/webapp/app/entities/chatmessage/chatmessage-my-suffix.service';
import { ChatMessageMySuffix } from '../../../../../../main/webapp/app/entities/chatmessage/chatmessage-my-suffix.model';

describe('Component Tests', () => {

    describe('ChatMessageMySuffix Management Detail Component', () => {
        let comp: ChatMessageMySuffixDetailComponent;
        let fixture: ComponentFixture<ChatMessageMySuffixDetailComponent>;
        let service: ChatMessageMySuffixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ChatTestModule],
                declarations: [ChatMessageMySuffixDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ChatMessageMySuffixService,
                    JhiEventManager
                ]
            }).overrideTemplate(ChatMessageMySuffixDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ChatMessageMySuffixDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ChatMessageMySuffixService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ChatMessageMySuffix(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.chatmessage).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
