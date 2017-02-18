import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Game } from './game.model';
import { GamePopupService } from './game-popup.service';
import { GameService } from './game.service';
import { Block, BlockService } from '../block';
import { CardType, CardTypeService } from '../card-type';
@Component({
    selector: 'jhi-game-dialog',
    templateUrl: './game-dialog.component.html'
})
export class GameDialogComponent implements OnInit {

    game: Game;
    authorities: any[];
    isSaving: boolean;

    blocks: Block[];

    cardtypes: CardType[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private gameService: GameService,
        private blockService: BlockService,
        private cardTypeService: CardTypeService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['game']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.blockService.query().subscribe(
            (res: Response) => { this.blocks = res.json(); }, (res: Response) => this.onError(res.json()));
        this.cardTypeService.query().subscribe(
            (res: Response) => { this.cardtypes = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.game.id !== undefined) {
            this.gameService.update(this.game)
                .subscribe((res: Game) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.gameService.create(this.game)
                .subscribe((res: Game) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Game) {
        this.eventManager.broadcast({ name: 'gameListModification', content: 'OK'});
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

    trackBlockById(index: number, item: Block) {
        return item.id;
    }

    trackCardTypeById(index: number, item: CardType) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-game-popup',
    template: ''
})
export class GamePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private gamePopupService: GamePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.gamePopupService
                    .open(GameDialogComponent, params['id']);
            } else {
                this.modalRef = this.gamePopupService
                    .open(GameDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
