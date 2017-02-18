import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { CardType } from './card-type.model';
import { CardTypePopupService } from './card-type-popup.service';
import { CardTypeService } from './card-type.service';
import { Game, GameService } from '../game';
@Component({
    selector: 'jhi-card-type-dialog',
    templateUrl: './card-type-dialog.component.html'
})
export class CardTypeDialogComponent implements OnInit {

    cardType: CardType;
    authorities: any[];
    isSaving: boolean;

    games: Game[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private cardTypeService: CardTypeService,
        private gameService: GameService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['cardType']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.gameService.query().subscribe(
            (res: Response) => { this.games = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.cardType.id !== undefined) {
            this.cardTypeService.update(this.cardType)
                .subscribe((res: CardType) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.cardTypeService.create(this.cardType)
                .subscribe((res: CardType) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: CardType) {
        this.eventManager.broadcast({ name: 'cardTypeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    trackGameById(index: number, item: Game) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-card-type-popup',
    template: ''
})
export class CardTypePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private cardTypePopupService: CardTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.cardTypePopupService
                    .open(CardTypeDialogComponent, params['id']);
            } else {
                this.modalRef = this.cardTypePopupService
                    .open(CardTypeDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
