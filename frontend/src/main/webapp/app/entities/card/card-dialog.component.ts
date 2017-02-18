import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Card } from './card.model';
import { CardPopupService } from './card-popup.service';
import { CardService } from './card.service';
import { CardType, CardTypeService } from '../card-type';
import { Reprint, ReprintService } from '../reprint';
@Component({
    selector: 'jhi-card-dialog',
    templateUrl: './card-dialog.component.html'
})
export class CardDialogComponent implements OnInit {

    card: Card;
    authorities: any[];
    isSaving: boolean;

    types: CardType[];

    reprints: Reprint[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private cardService: CardService,
        private cardTypeService: CardTypeService,
        private reprintService: ReprintService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['card', 'rarity']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.cardTypeService.query({filter: 'card-is-null'}).subscribe((res: Response) => {
            if (!this.card.typeId) {
                this.types = res.json();
            } else {
                this.cardTypeService.find(this.card.typeId).subscribe((subRes: CardType) => {
                    this.types = [subRes].concat(res.json());
                }, (subRes: Response) => this.onError(subRes.json()));
            }
        }, (res: Response) => this.onError(res.json()));
        this.reprintService.query().subscribe(
            (res: Response) => { this.reprints = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.card.id !== undefined) {
            this.cardService.update(this.card)
                .subscribe((res: Card) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.cardService.create(this.card)
                .subscribe((res: Card) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Card) {
        this.eventManager.broadcast({ name: 'cardListModification', content: 'OK'});
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

    trackCardTypeById(index: number, item: CardType) {
        return item.id;
    }

    trackReprintById(index: number, item: Reprint) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-card-popup',
    template: ''
})
export class CardPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private cardPopupService: CardPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.cardPopupService
                    .open(CardDialogComponent, params['id']);
            } else {
                this.modalRef = this.cardPopupService
                    .open(CardDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
