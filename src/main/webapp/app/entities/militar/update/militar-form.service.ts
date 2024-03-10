import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMilitar, NewMilitar } from '../militar.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMilitar for edit and NewMilitarFormGroupInput for create.
 */
type MilitarFormGroupInput = IMilitar | PartialWithRequiredKeyOf<NewMilitar>;

type MilitarFormDefaults = Pick<NewMilitar, 'id'>;

type MilitarFormGroupContent = {
  id: FormControl<IMilitar['id'] | NewMilitar['id']>;
  identidade: FormControl<IMilitar['identidade']>;
  cpf: FormControl<IMilitar['cpf']>;
  postoGraduacao: FormControl<IMilitar['postoGraduacao']>;
  nome: FormControl<IMilitar['nome']>;
  nomeGuerra: FormControl<IMilitar['nomeGuerra']>;
  email: FormControl<IMilitar['email']>;
  foto: FormControl<IMilitar['foto']>;
  fotoContentType: FormControl<IMilitar['fotoContentType']>;
  posto: FormControl<IMilitar['posto']>;
  om: FormControl<IMilitar['om']>;
};

export type MilitarFormGroup = FormGroup<MilitarFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MilitarFormService {
  createMilitarFormGroup(militar: MilitarFormGroupInput = { id: null }): MilitarFormGroup {
    const militarRawValue = {
      ...this.getFormDefaults(),
      ...militar,
    };
    return new FormGroup<MilitarFormGroupContent>({
      id: new FormControl(
        { value: militarRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      identidade: new FormControl(militarRawValue.identidade, {
        validators: [Validators.required],
      }),
      cpf: new FormControl(militarRawValue.cpf, {
        validators: [Validators.required],
      }),
      postoGraduacao: new FormControl(militarRawValue.postoGraduacao, {
        validators: [Validators.required],
      }),
      nome: new FormControl(militarRawValue.nome, {
        validators: [Validators.required],
      }),
      nomeGuerra: new FormControl(militarRawValue.nomeGuerra, {
        validators: [Validators.required],
      }),
      email: new FormControl(militarRawValue.email, {
        validators: [Validators.required],
      }),
      foto: new FormControl(militarRawValue.foto, {
        validators: [Validators.required],
      }),
      fotoContentType: new FormControl(militarRawValue.fotoContentType),
      posto: new FormControl(militarRawValue.posto, {
        validators: [Validators.required],
      }),
      om: new FormControl(militarRawValue.om, {
        validators: [Validators.required],
      }),
    });
  }

  getMilitar(form: MilitarFormGroup): IMilitar | NewMilitar {
    return form.getRawValue() as IMilitar | NewMilitar;
  }

  resetForm(form: MilitarFormGroup, militar: MilitarFormGroupInput): void {
    const militarRawValue = { ...this.getFormDefaults(), ...militar };
    form.reset(
      {
        ...militarRawValue,
        id: { value: militarRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MilitarFormDefaults {
    return {
      id: null,
    };
  }
}
