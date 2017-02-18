import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Edition } from './edition.model';
import { EditionPopupService } from './edition-popup.service';
import { EditionService } from './edition.service';
import { Card, CardService } from '../card';
import { Block, BlockService } from '../block';
@Component({
    selector: 'jhi-edition-dialog',
    templateUrl: './edition-dialog.component.html'
})
export class EditionDialogComponent implements OnInit {

    edition: Edition;
    authorities: any[];
    isSaving: boolean;

    cards: Card[];

    blocks: Block[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private editionService: EditionService,
        private cardService: CardService,
        private blockService: BlockService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['edition']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.cardService.query().subscribe(
            (res: Response) => { this.cards = res.json(); }, (res: Response) => this.onError(res.json()));
        this.blockService.query().subscribe(
            (res: Response) => { this.blocks = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.edition.id !== undefined) {
            this.editionService.update(this.edition)
                .subscribe((res: Edition) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.editionService.create(this.edition)
                .subscribe((res: Edition) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Edition) {
        this.eventManager.broadcast({ name: 'editionListModification', content: 'OK'});
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

    trackCardById(index: number, item: Card) {
        return item.id;
    }

    trackBlockById(index: number, item: Block) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-edition-popup',
    template: ''
})
export class EditionPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private editionPopupService: EditionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.editionPopupService
                    .open(EditionDialogComponent, params['id']);
            } else {
                this.modalRef = this.editionPopupService
                    .open(EditionDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
